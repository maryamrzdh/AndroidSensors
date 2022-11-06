package com.example.hw2

import android.content.Context
import android.graphics.Camera
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SensorFragment : Fragment(),
    SensorEventListener {

    var camManager: CameraManager? =null
    lateinit var cameraId: String
    var mLight: Sensor?=null
    var mProximity: Sensor?=null

    lateinit var sensorManager: SensorManager

    private val SENSOR_SENSITIVITY = 4
    private val SENSOR_SENSITIVITY_light = 10


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sensor, container, false)

        sensorManager = requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager


        val lightSwitch = view.findViewById<Switch>(R.id.switch_light)
        val vicinitySwitch = view.findViewById<Switch>(R.id.switch_vicinity)


        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        mProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        camManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        cameraId = camManager?.cameraIdList?.get(0)?:"" // Usually front camera is at 0 position.


        lightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            // TODO: if aproximity is available
            if (isChecked){

                vicinitySwitch.isChecked = false

                sensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL)


            }else
                sensorManager.unregisterListener(this)

        }


        vicinitySwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){

                lightSwitch.isChecked = false
                sensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL)

            }else
                sensorManager.unregisterListener(this)

        }



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

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        when(event?.sensor?.type){
            Sensor.TYPE_PROXIMITY->{
                if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                    //near
                    Toast.makeText(requireContext(), "near", Toast.LENGTH_SHORT).show()
                    turnOn()
                } else {
                    //far
//                    Toast.makeText(requireContext(), "far", Toast.LENGTH_SHORT).show()
                    turnOff()
                }
            }
            Sensor.TYPE_LIGHT ->{
                if ( event.values[0] <= SENSOR_SENSITIVITY_light) {
                    //near
                    Toast.makeText(requireContext(), "dark", Toast.LENGTH_SHORT).show()
                    turnOn()
                } else {
                    //far
//                    Toast.makeText(requireContext(), "normal", Toast.LENGTH_SHORT).show()
                    turnOff()
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}


    fun turnOn() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            camManager?.setTorchMode(cameraId, true)
        }
    }

    private fun turnOff(){
        val cameraId: String = camManager?.cameraIdList?.get(0)?:"" // Usually front camera is at 0 position.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            camManager?.setTorchMode(cameraId, false)
        }
    }

}