package com.example.hw2.stepCounter

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hw2.App
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StepCounterViewModel : ViewModel(),SensorEventListener {

    // Creating a variable which will counts total steps
    // and it has been given the value of 0 float
    var totalSteps = 0f

    // Creating a variable  which counts previous total
    // steps and it has also been given the value of 0 float
    var previousTotalSteps = 0f

    private var stepCounter = MutableStateFlow(0)
    val stepCount = stepCounter.asStateFlow()

    override fun onSensorChanged(event: SensorEvent?) {

//        if (running) {
        totalSteps = event!!.values[0]

        // Current steps are calculated by taking the difference of total steps
        // and previous steps
        stepCounter.value = totalSteps.toInt() - previousTotalSteps.toInt()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}


     fun saveData() {

        // Shared Preferences will allow us to save
        // and retrieve data in the form of key,value pair.
        // In this function we will save data
//        App.saveShay("key1" , previousTotalSteps)
        App.saveShay("key1" , totalSteps)
    }

    fun loadData() {

        // In this function we will retrieve data
        val savedNumber = App.loadShay("key1",0f)

        // Log.d is used for debugging purposes
        Log.d("StepCounter", "$savedNumber")

        previousTotalSteps = savedNumber
    }
}