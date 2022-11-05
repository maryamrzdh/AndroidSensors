package com.example.hw2

import android.hardware.*
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SensorActivity : AppCompatActivity(),SensorEventListener {

    private lateinit var sensorManager: SensorManager
    lateinit var light: Sensor

    lateinit var tv :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

         tv = findViewById(R.id.tv_sensor)

        sensorManager = this.getSystemService(SENSOR_SERVICE) as SensorManager

        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        sensorManager.registerListener(this, light,SensorManager.SENSOR_DELAY_NORMAL);

    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {


    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        tv.text= p1.toString()
    }


}