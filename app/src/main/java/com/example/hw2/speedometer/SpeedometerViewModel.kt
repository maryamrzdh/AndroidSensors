package com.example.hw2.speedometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.lifecycle.ViewModel
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class SpeedometerViewModel:ViewModel() ,SensorEventListener {

    private var _lastTick = System.currentTimeMillis()
    private var currentTimeMillis = System.currentTimeMillis()

    private val _samplePeriod = 15

    private var maxSpeed = 0f

    override fun onSensorChanged(p0: SensorEvent?) {
//
//        // val vendor: String = event?.sensor?.vendor!!
//        // Toast.makeText(ActivitySensor.this, vendor, Toast.LENGTH_SHORT).show();
//        // Toast.makeText(ActivitySensor.this, vendor, Toast.LENGTH_SHORT).show();
//        if (event?.sensor?.type == Sensor.TYPE_LINEAR_ACCELERATION) {
//            val tick = System.currentTimeMillis()
//            val localPeriod: Long = tick - _lastTick
//            if (localPeriod > _samplePeriod) {
//                _lastTick = tick
//                val motion = sqrt(
//                    event.values[0].toDouble().pow(2.0) +
//                            event.values[1].toDouble().pow(2.0) +
//                            event.values[2].toDouble().pow(2.0)
//                )
//
//
//
//                // Warn the activity that we sampled a new value.
//                tvMaxSpeed.text = motion.toString() + ""
//
//
//                val speedKM = (motion * 3.6).toFloat()
//
//                //mh
////                val dT: Float = (event.timestamp - timestamp) / 1000000000.0f
////                timestamp = event.timestamp
////todo rotate clear the app use viewModel
//                maxSpeed = max(maxSpeed , speedKM)
//                tvMaxSpeed.text = maxSpeed.toString()
//
////                currentTimeMillis.minutes
//                tvTime.text = (event.timestamp - currentTimeMillis).toString()
//////////////
////                speedometer.onSpeedChanged(speedKM)
//                if (speedKM > 50) {
////                    speedometer.setBackgroundColor(resources.getColor(R.color.red))
////                    tvSpeed.text = "HIGH"
////                    tvSensor.setTextColor(resources.getColor(R.color.red))
//                } else if (speedKM > 25) {
////                    tvSpeed.text = "AVERAGE"
////                    tvSensor.setTextColor(resources.getColor(R.color.green))
////                    speedometer.setBackgroundColor(resources.getColor(R.color.green))
//                } else {
////                    tvSpeed.text = "LOW"
////                    tvSensor.setTextColor(resources.getColor(R.color.blue))
////                    speedometer.setBackgroundColor(resources.getColor(R.color.blue))
//                }
//            }
//        }




    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

}