package com.example.hw2.stepCounter

import android.app.Notification
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

    private val notificationId = 1001


    private val channelId = "Notification from Service"

    private lateinit var builder : Notification.Builder
    private lateinit var builderCompat : NotificationCompat.Builder
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

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        showNotification()

        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null)
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        else
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)

        return START_NOT_STICKY
    }

    private fun showNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW)

            builder = Notification.Builder(this,channelId)
                .setContentTitle("your steps counts is ...")
                .setContentText("service is running")
                .setSmallIcon(android.R.drawable.ic_dialog_info)

            notificationManager.createNotificationChannel(channel)

        }else{
            builderCompat = NotificationCompat.Builder(this,channelId)
                .setContentTitle("your steps counts is ...")
                .setContentText("service is running")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){

            totalSteps = event!!.values[0]

            //As you can see, while the sensor is activated, the value will keep increasing
            // without resetting to zero until the system is rebooted.

            // Current steps are calculated by taking the difference of total steps
            // and previous steps

            var currentSteps = 0
            if (previousTotalSteps == 0f)
                previousTotalSteps = totalSteps

                currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()

            scope.launch {
                EventBus.getDefault().post(StepEvent("$currentSteps"))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder.setContentText("$currentSteps")
                    notificationManager.notify(notificationId, builder.build())
                }
                else{
                    builderCompat.setContentText("$currentSteps")
                    notificationManager.notify(notificationId, builderCompat.build())
                }
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