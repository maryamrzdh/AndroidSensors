package com.example.hw2

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
import android.widget.Toast
import kotlin.math.max

class SpeedometerFragment : BaseFragment() ,SensorEventListener  {

    var sensor: Sensor? = null

    lateinit var tvSpeed :TextView
    lateinit var tvMaxSpeed :TextView
    lateinit var tvAverage :TextView
    lateinit var tvDistance :TextView
    lateinit var tvTime :TextView
    lateinit var tvAccuraccy :TextView

    private var _lastTick = System.currentTimeMillis()
    var currentTimeMillis = System.currentTimeMillis()

    private val _samplePeriod = 15

    var maxSpeed = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_speedometer, container, false)

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        tvSpeed = view.findViewById(R.id.tv_speed)
        tvMaxSpeed = view.findViewById(R.id.tv_max_speed)
        tvAverage = view.findViewById(R.id.tv_average)
        tvDistance = view.findViewById(R.id.tv_distance)
        tvTime = view.findViewById(R.id.tv_time)
        tvAccuraccy = view.findViewById(R.id.tv_accuracy)

        val btnStart = view.findViewById<Button>(R.id.btn_start)
        val btnEnd = view.findViewById<Button>(R.id.btn_end)

        btnStart.setOnClickListener {
            btnStart.visibility = View.GONE
            btnEnd.visibility = View.VISIBLE

            if (sensor == null) {
                // This will give a toast message to the user if there is no sensor in the device
                Toast.makeText(requireContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show()
            } else {
                // Rate suitable for the user interface
//                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

            }
        }

        btnEnd.setOnClickListener {
            btnEnd.visibility = View.GONE
            btnStart.visibility = View.VISIBLE

            clearForm()
            sensorManager.unregisterListener(this)
        }

        return view
    }

    fun clearForm(){
        tvSpeed.text = ""
        tvMaxSpeed.text = ""
        tvAverage.text = ""
        tvDistance.text = ""
        tvTime.text = ""
        tvAccuraccy.text = ""
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val vendor: String = event?.sensor?.vendor!!
        // Toast.makeText(ActivitySensor.this, vendor, Toast.LENGTH_SHORT).show();
        // Toast.makeText(ActivitySensor.this, vendor, Toast.LENGTH_SHORT).show();
        if (event?.sensor?.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val tick = System.currentTimeMillis()
            val localPeriod: Long = tick - _lastTick
            if (localPeriod > _samplePeriod) {
                _lastTick = tick
                val motion = Math.sqrt(
                    Math.pow(event.values.get(0).toDouble(), 2.0) +
                            Math.pow(event.values.get(1).toDouble(), 2.0) +
                            Math.pow(event.values.get(2).toDouble(), 2.0)
                )



                // Warn the activity that we sampled a new value.
                tvMaxSpeed.text = motion.toString() + ""


                val speedKM = (motion * 3.6).toFloat()

                //mh
//                val dT: Float = (event.timestamp - timestamp) / 1000000000.0f
//                timestamp = event.timestamp
//todo rotate clear the app use viewmodel
                maxSpeed = max(maxSpeed , speedKM)
                tvMaxSpeed.text = maxSpeed.toString()

//                currentTimeMillis.minutes
                tvTime.text = (event.timestamp - currentTimeMillis).toString()
////////////
//                speedometer.onSpeedChanged(speedKM)
                if (speedKM > 50) {
//                    speedometer.setBackgroundColor(resources.getColor(R.color.red))
                    tvSpeed.text = "HIGH"
//                    tvSensor.setTextColor(resources.getColor(R.color.red))
                } else if (speedKM > 25) {
                    tvSpeed.text = "AVERAGE"
//                    tvSensor.setTextColor(resources.getColor(R.color.green))
//                    speedometer.setBackgroundColor(resources.getColor(R.color.green))
                } else {
                    tvSpeed.text = "LOW"
//                    tvSensor.setTextColor(resources.getColor(R.color.blue))
//                    speedometer.setBackgroundColor(resources.getColor(R.color.blue))
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}