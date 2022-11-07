package com.example.hw2

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class StepCounterFragment : Fragment(), SensorEventListener {

    // Added SensorEventListener the MainActivity class
    // Implement all the members in the class MainActivity
    // after adding SensorEventListener

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

    private lateinit var tvStepsTaken: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_step_counter, container, false)

        val btnStart = view.findViewById<Button>(R.id.btn_start)
        val btnEnd = view.findViewById<Button>(R.id.btn_end)
        tvStepsTaken = view.findViewById(R.id.tv_stepsTaken)

        // Adding a context of SENSOR_SERVICE aas Sensor Manager
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        btnStart.setOnClickListener {
            btnStart.visibility = View.GONE
            btnEnd.visibility = View.VISIBLE

            startService(view)

            if (stepSensor == null) {
                // This will give a toast message to the user if there is no sensor in the device
                Toast.makeText(requireContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show()
            } else {
                // Rate suitable for the user interface
                sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            }
        }

        btnEnd.setOnClickListener {
            btnEnd.visibility = View.GONE
            btnStart.visibility = View.VISIBLE

            sensorManager?.unregisterListener(this)

            stopMyService(view)
        }

        loadData()
        resetSteps()


        return view
    }

    override fun onResume() {
        super.onResume()
        running = true

        // Returns the number of steps taken by the user since the last reboot while activated
        // This sensor requires permission android.permission.ACTIVITY_RECOGNITION.
        // So don't forget to add the following permission in AndroidManifest.xml present in manifest folder of the app.


    }

    override fun onSensorChanged(event: SensorEvent?) {

        // Calling the TextView that we made in activity_main.xml
        // by the id given to that TextView

        if (running) {
            totalSteps = event!!.values[0]

            // Current steps are calculated by taking the difference of total steps
            // and previous steps
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()

            // It will show the current steps to the user
            tvStepsTaken.text = ("$currentSteps")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private fun resetSteps() {
        tvStepsTaken.setOnClickListener {
            // This will give a toast message if the user want to reset the steps
            Toast.makeText(requireContext(), "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        tvStepsTaken.setOnLongClickListener {

            previousTotalSteps = totalSteps

            // When the user will click long tap on the screen,
            // the steps will be reset to 0
            tvStepsTaken.text = 0.toString()

            // This will save the data
            saveData()

            true
        }
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

    fun startService(view: View) {
        val input: String = tvStepsTaken.text.toString()
        val serviceIntent = Intent(requireActivity(), StepCounterService::class.java)
        serviceIntent.putExtra("inputExtra", "your step count is $input")
        ContextCompat.startForegroundService(requireContext(), serviceIntent)
    }
    fun stopMyService(view: View) {
        val serviceIntent = Intent(requireActivity(), StepCounterService::class.java)
        requireActivity().stopService(serviceIntent)
    }
}