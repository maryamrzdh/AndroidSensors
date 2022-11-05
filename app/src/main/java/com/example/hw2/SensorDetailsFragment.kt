package com.example.hw2

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import org.w3c.dom.Text

class SensorDetailsFragment :  BaseFragment() , SensorEventListener {
    var sensor: Sensor?=null

    lateinit var tv : TextView

    val args : SensorDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sensor_details, container, false)
        val sensorManager = requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager

        tv = view.findViewById(R.id.tv_sensor_desc)

        when(args.sensorType){
            "light" ->sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        }

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        return view
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light1 = event.values[0]

            tv.text = "Sensor: $light1"
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}