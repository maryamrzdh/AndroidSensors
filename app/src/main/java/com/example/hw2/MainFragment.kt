package com.example.hw2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.karlveskus.ballgame.SimpleGameActivity

class MainFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSensor = activity?.findViewById<Button>(R.id.btn_sensors)
        val btnGame = activity?.findViewById<Button>(R.id.btn_game)
        btnSensor!!.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_sensorFragment)
        }

        btnGame?.setOnClickListener {
            startActivity(Intent(activity,SimpleGameActivity::class.java))
        }
    }
}