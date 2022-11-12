package com.example.hw2.stepCounter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.hw2.App
import com.example.hw2.R
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus


class StepCounterService : Service() ,SensorEventListener {

    // Added SensorEventListener the MainActivity class
    // Implement all the members in the class MainActivity
    // after adding SensorEventListener

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO+job)

    // we have assigned sensorManger to nullable
    private var sensorManager: SensorManager? = null


    // Creating a variable which will give the running status
    // and initially given the boolean value as false
    private var running = false

    // Creating a variable which will counts total steps
    // and it has been given the value of 0 float
    private var totalSteps = 0f

    // Creating a variable  which counts previous total
    // steps and it has also been given the value of 0 float
    private var previousTotalSteps = 0f

    private val channelId = "Notification from Service"

    private lateinit var builder : NotificationCompat.Builder
    private lateinit var notificationManager : NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        // Adding a context of SENSOR_SERVICE aas Sensor Manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        loadData()

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        running = true


        // Returns the number of steps taken by the user since the last reboot while activated
        // This sensor requires permission android.permission.ACTIVITY_RECOGNITION.
        // So don't forget to add the following permission in AndroidManifest.xml present in manifest folder of the app.
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            // This will give a toast message to the user if there is no sensor in the device
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            // Rate suitable for the user interface
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,channelId,NotificationManager.IMPORTANCE_LOW)
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this,channelId)
                .setContentTitle("service enable")
                .setContentText("service is running")
                .setSmallIcon(R.drawable.ic_launcher_background)
//            startForeground(1001,builder.build())
        }
        return START_NOT_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            totalSteps = event!!.values[0]

            // Current steps are calculated by taking the difference of total steps
            // and previous steps
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()

            scope.launch {
                EventBus.getDefault().post(StepEvent("$currentSteps"))
                delay(2000)
                builder.setContentText(currentSteps.toString())
                notificationManager.notify(1001, builder.build())
            }
        }

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        sensorManager?.unregisterListener(this)
        previousTotalSteps = totalSteps
        saveData()
    }

    private fun saveData() {

        // Shared Preferences will allow us to save
        // and retrieve data in the form of key,value pair.
        // In this function we will save data
        App.saveShay("key1" , previousTotalSteps)
    }

    private fun loadData() {

        // In this function we will retrieve data
        val savedNumber = App.loadShay("key1",0f)

        // Log.d is used for debugging purposes
        Log.d("StepCounter", "$savedNumber")

        previousTotalSteps = savedNumber
    }
}