package com.example.hw2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.karlveskus.ballgame.SimpleGameActivity

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val btnSensor = view.findViewById<Button>(R.id.btn_sensors)
        val btnGame = view.findViewById<Button>(R.id.btn_game)
        val btnStepCounter = view.findViewById<Button>(R.id.btn_step_counter)
        val btnSpeedometer = view.findViewById<Button>(R.id.btn_speedometer)

        btnSensor.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_sensorFragment)
        }

        btnGame.setOnClickListener {
            startActivity(Intent(activity,SimpleGameActivity::class.java))
        }

        btnStepCounter.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_stepCounterFragment)
        }

        btnSpeedometer.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_speedometerFragment)
        }
        return view
    }
}