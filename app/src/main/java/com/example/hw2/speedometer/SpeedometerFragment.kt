package com.example.hw2.speedometer

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.hw2.BaseFragment
import com.example.hw2.R

class SpeedometerFragment : BaseFragment() {

    private lateinit var tvSpeed :TextView
    private lateinit var tvMaxSpeed :TextView
    private lateinit var tvAverage :TextView
    private lateinit var tvDistance :TextView
    private lateinit var tvTime :TextView
    private lateinit var tvAccuracy :TextView
    private lateinit var viewModel: SpeedometerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_speedometer, container, false)

        viewModel = ViewModelProvider(this)[SpeedometerViewModel::class.java]

        getSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        tvSpeed = view.findViewById(R.id.tv_speed)
        tvMaxSpeed = view.findViewById(R.id.tv_max_speed)
        tvAverage = view.findViewById(R.id.tv_average)
        tvDistance = view.findViewById(R.id.tv_distance)
        tvTime = view.findViewById(R.id.tv_time)
        tvAccuracy = view.findViewById(R.id.tv_accuracy)

        val btnStart = view.findViewById<Button>(R.id.btn_start)
        val btnEnd = view.findViewById<Button>(R.id.btn_end)

        btnStart.setOnClickListener {
            btnStart.visibility = View.GONE
            btnEnd.visibility = View.VISIBLE

            if (sensor != null) {
                // Rate suitable for the user interface
//                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
                sensorManager?.registerListener(viewModel, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                // This will give a toast message to the user if there is no sensor in the device
                Toast.makeText(requireContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show()
            }
        }

        btnEnd.setOnClickListener {
            btnEnd.visibility = View.GONE
            btnStart.visibility = View.VISIBLE

            clearForm()
            sensorManager?.unregisterListener(viewModel)
        }

        return view
    }

    private fun clearForm(){
        tvSpeed.text = ""
        tvMaxSpeed.text = ""
        tvAverage.text = ""
        tvDistance.text = ""
        tvTime.text = ""
        tvAccuracy.text = ""
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(viewModel)
    }
}