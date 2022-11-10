package com.example.hw2

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    var sensorManager: SensorManager?=null
    var sensor: Sensor?=null

    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
        super.onCreate(savedInstanceState)

        sensorManager = requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
    }

    fun getSensor(sensorType:Int):Sensor?{
        sensor = sensorManager?.getDefaultSensor(sensorType)
        return sensor
    }

}