package com.example.hw2.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hw2.BaseFragment
import com.example.hw2.R
import com.example.hw2.getSensorDesc
import com.example.hw2.getSensorName

class SensorDetailsFragment :  BaseFragment() , SensorEventListener {

    private lateinit var tvTitle : TextView
    private lateinit var tvValue : TextView
    private lateinit var tvDesc : TextView

    private val args : SensorDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sensor_details, container, false)

        val btnBack = view.findViewById<Button>(R.id.btn_back)
        tvTitle = view.findViewById(R.id.tv_title)
        tvValue = view.findViewById(R.id.tv_sensor_value)
        tvDesc = view.findViewById(R.id.tv_desc)

        getSensor(args.sensorType)

        if (sensor!=null) {
            tvTitle.text = sensor?.getSensorName(requireContext())
            tvDesc.text = sensor?.getSensorDesc(requireContext())
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

    override fun onSensorChanged(event: SensorEvent?) {
        tvValue.text = event?.values?.get(0).toString()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (sensor != null) {
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}