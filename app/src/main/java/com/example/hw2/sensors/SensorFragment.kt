package com.example.hw2.sensors

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hw2.BaseFragment
import com.example.hw2.R

class SensorFragment : BaseFragment(),
    SensorEventListener {

    private var camManager: CameraManager? =null
    private lateinit var cameraId: String
    private var mLight: Sensor?=null
    private var mProximity: Sensor?=null
    private val sensorSensitivity = 4
    private val sensorSensitivityLight = 10


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sensor, container, false)

        val lightSwitch = view.findViewById<Switch>(R.id.switch_light)
        val proximitySwitch = view.findViewById<Switch>(R.id.switch_vicinity)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_sensors)

        mLight = getSensor(Sensor.TYPE_LIGHT)
        mProximity = getSensor(Sensor.TYPE_PROXIMITY)

        camManager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        cameraId = camManager?.cameraIdList?.get(0)?:"" // Usually front camera is at 0 position.

        lightSwitch.setOnCheckedChangeListener { _, isChecked ->

            if (mLight!=null){
                if (isChecked){
                    proximitySwitch.isChecked = false
                    sensorManager?.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL)
                }else {
                    turnOff()
                    sensorManager?.unregisterListener(this)
                }
            }
            else
                Toast.makeText(requireContext(), "No light sensor detected on this device", Toast.LENGTH_SHORT).show()
        }

        proximitySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (mProximity != null) {
                if (isChecked) {
                    lightSwitch.isChecked = false
                    sensorManager?.registerListener(
                        this,
                        mProximity,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                } else {
                    turnOff()
                    sensorManager?.unregisterListener(this)
                }
            } else
                Toast.makeText(
                    requireContext(),
                    "No proximity sensor detected on this device",
                    Toast.LENGTH_SHORT
                ).show()
        }

        val list: List<Sensor> = sensorManager?.getSensorList(Sensor.TYPE_ALL)?: emptyList()

        recyclerView.apply {
            adapter = SensorsAdapter(requireContext(),list) { sensor ->
                val direction =
                    SensorFragmentDirections.actionSensorFragmentToSensorDetailsFragment(sensor.type)
                findNavController().navigate(direction)
            }

            layoutManager = GridLayoutManager(requireContext(),2)
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        when(event?.sensor?.type){
            Sensor.TYPE_PROXIMITY->{
                if (event.values[0] >= -sensorSensitivity && event.values[0] <= sensorSensitivity) {
                    turnOn()
                } else {
                    turnOff()
                }
            }
            Sensor.TYPE_LIGHT ->{
                if ( event.values[0] <= sensorSensitivityLight) {
                    Log.d("TAG", "onSensorChanged: ${event.values[0] }")
                    turnOn()
                } else {
                    turnOff()
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private fun turnOn() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                camManager?.setTorchMode(cameraId, true)
            }
        }catch (ex:java.lang.Exception){
            Toast.makeText(requireContext(),"${ex.message}" , Toast.LENGTH_SHORT).show()
        }

    }

    private fun turnOff(){
        val cameraId: String = camManager?.cameraIdList?.get(0)?:""

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                camManager?.setTorchMode(cameraId, false)
            }
        }catch (ex:java.lang.Exception){
            Toast.makeText(requireContext(),"${ex.message}" , Toast.LENGTH_SHORT).show()
        }

    }

}