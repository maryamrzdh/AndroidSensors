package com.example.hw2

import android.content.pm.PackageManager
import android.graphics.Camera
import android.graphics.SurfaceTexture
import android.hardware.Camera.open
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.nio.channels.DatagramChannel.open


class SensorFragment : BaseFragment()  {

    lateinit var camera: Camera

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sensor, container, false)

        val sensorManager = requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager

        val list: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in list) {
            Log.d("TAG", "onCreateView: $sensor ")

        }
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_sensors)
        recyclerView.apply {
            adapter = SensorsAdapter(list) { sensor ->
                val direction =
                    SensorFragmentDirections.actionSensorFragmentToSensorDetailsFragment(sensor.type)
                findNavController().navigate(direction)
            }

            layoutManager = GridLayoutManager(requireContext(),2)
        }







        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    fun turnFlashLightOn(flag:Boolean){
//
//        camera = Camera.open()
//        val p: Parameters = cam.getParameters()
//        p.setFlashMode(Parameters.FLASH_MODE_TORCH)
//        cam.setParameters(p)
//        cam.startPreview()
//        //is flashLight available
//        if(requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
//        }
//
//    }

//    fun turnOn() {
//        camera = Camera.open()
//        try {
//            val parameters: Parameters = camera.getParameters()
//            parameters.setFlashMode(getFlashOnParameter())
//            camera.setParameters(parameters)
//            camera.setPreviewTexture(SurfaceTexture(0))
//            camera.startPreview()
//            camera.autoFocus(this)
//        } catch (e: Exception) {
//            // We are expecting this to happen on devices that don't support autofocus.
//        }
//    }
}