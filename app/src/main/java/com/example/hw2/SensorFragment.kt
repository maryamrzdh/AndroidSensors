package com.example.hw2


import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

class SensorFragment : BaseFragment()  {

     var btn : Button? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sensor, container, false)

        val sensorManager = requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager

        btn = view.findViewById<Button>(R.id.btn_light)

        val list: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in list) {
            Log.d("TAG", "onCreateView: $sensor ")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val direction = SensorFragmentDirections.actionSensorFragmentToSensorDetailsFragment("light")

        btn?.setOnClickListener {
            findNavController().navigate(direction)
        }
    }
}