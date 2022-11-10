package com.example.hw2.stepCounter

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hw2.BaseFragment
import com.example.hw2.R
import kotlinx.coroutines.launch

class StepCounterFragment : BaseFragment() {

    private lateinit var viewModel: StepCounterViewModel
    // Added SensorEventListener the MainActivity class
    // Implement all the members in the class MainActivity
    // after adding SensorEventListener

    // Creating a variable which will give the running status
    // and initially given the boolean value as false
//    private var running = false

    private lateinit var btnEnd : Button
    private lateinit var btnStart : Button

    private lateinit var tvStepsTaken: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step_counter, container, false)

        viewModel = ViewModelProvider(this)[StepCounterViewModel::class.java]

        btnStart = view.findViewById(R.id.btn_start)
        btnEnd = view.findViewById(R.id.btn_end)
        tvStepsTaken = view.findViewById(R.id.tv_stepsTaken)

        getSensor(Sensor.TYPE_STEP_COUNTER)

        btnStart.setOnClickListener {

            if (sensor == null) {
                // This will give a toast message to the user if there is no sensor in the device
                Toast.makeText(requireContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show()
            } else {
                // Rate suitable for the user interface
                sensorManager?.registerListener(viewModel, sensor, SensorManager.SENSOR_DELAY_UI)
                btnStart.visibility = View.GONE
                btnEnd.visibility = View.VISIBLE

                startService()
            }
        }

        viewModel.loadData()
        resetSteps()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.stepCount.collect {
                tvStepsTaken.text = ("$it")
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
////        running = true
////        if (sensor!=null)
////            sensorManager?.registerListener(viewModel, sensor, SensorManager.SENSOR_DELAY_UI)
//    }

    private fun resetSteps() {
        btnEnd.setOnClickListener {
            Toast.makeText(requireContext(), "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        btnEnd.setOnLongClickListener {
            btnEnd.visibility = View.GONE
            btnStart.visibility = View.VISIBLE

//            previousTotalSteps = totalSteps

            // When the user will click long tap on the screen,
            // the steps will be reset to 0
            tvStepsTaken.text = 0.toString()

            // This will save the data
            viewModel.saveData()

            sensorManager?.unregisterListener(viewModel)

            stopMyService()

            true
        }
    }

    private fun startService() {
        val input: String = tvStepsTaken.text.toString()
        val serviceIntent = Intent(requireActivity(), StepCounterService::class.java)
        serviceIntent.putExtra("inputExtra", "your step count is $input")
        ContextCompat.startForegroundService(requireContext(), serviceIntent)
    }

    private fun stopMyService() {
        val serviceIntent = Intent(requireActivity(), StepCounterService::class.java)
        requireActivity().stopService(serviceIntent)
    }

    override fun onDetach() {
        super.onDetach()
        sensorManager?.unregisterListener(viewModel)
    }
}