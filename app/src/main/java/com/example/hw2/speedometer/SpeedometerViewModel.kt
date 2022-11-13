package com.example.hw2.speedometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class SpeedometerViewModel:ViewModel() ,SensorEventListener {

    private var _currentSpeed = MutableStateFlow(0f)
    var currentSpeed = _currentSpeed.asStateFlow()

    private var _maxSpeed = MutableStateFlow("")
    var maxSpeedState = _maxSpeed.asStateFlow()

    private var _time = MutableStateFlow("")
    var time = _time.asStateFlow()


    private var _accuracy = MutableStateFlow("")
    var accuracy = _time.asStateFlow()

    private var _lastTick = System.currentTimeMillis()
    private var currentTimeMillis = System.currentTimeMillis()
    private val _samplePeriod = 15
    private var maxSpeed = 0f

    override fun onSensorChanged(event: SensorEvent?) {

        // val vendor: String = event?.sensor?.vendor!!

        if (event?.sensor?.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val tick = System.currentTimeMillis()
            val localPeriod: Long = tick - _lastTick
            if (localPeriod > _samplePeriod) {
                _lastTick = tick
                val motion = sqrt(
                    event.values[0].toDouble().pow(2.0) +
                            event.values[1].toDouble().pow(2.0) +
                            event.values[2].toDouble().pow(2.0)
                )

                var speedKM = (motion * 3.6).toFloat()

                val a  = DecimalFormat("##.##").format(speedKM).toFloat()

//                val dT: Float = (event.timestamp - timestamp) / 1000000000.0f

                maxSpeed = max(maxSpeed , a)

//                _currentSpeed.value = motion.toString() + ""
                _currentSpeed.value = a
                _maxSpeed.value =maxSpeed.toString()

//                todo currentTimeMillis.minutes
                _time.value = (event.timestamp - currentTimeMillis).toString()

                event.sensor.vendor
                _accuracy.value = event.accuracy.toString()

            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

}