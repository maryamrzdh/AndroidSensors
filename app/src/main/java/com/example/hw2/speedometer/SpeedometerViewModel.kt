package com.example.hw2.speedometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

class SpeedometerViewModel:ViewModel() ,SensorEventListener {

    private var sum = 0f
    private var count = 0
    private var sensorTimeReference = 0L
    private var myTimeReference  = 0L


    private var _currentSpeed = MutableStateFlow(0f)
    var currentSpeed = _currentSpeed.asStateFlow()

    private var _maxSpeed = MutableStateFlow("")
    var maxSpeedState = _maxSpeed.asStateFlow()

    private var _average = MutableStateFlow("")
    var average = _average.asStateFlow()

    private var _time = MutableStateFlow("")
    var time = _time.asStateFlow()

    private var _accuracy = MutableStateFlow("")
    var accuracy = _accuracy.asStateFlow()

    private var _lastTick = System.currentTimeMillis()
    private var currentTimeMillis = System.currentTimeMillis()
    private val _samplePeriod = 15
    private var maxSpeed = 0f

    override fun onSensorChanged(event: SensorEvent?) {

        count+=1
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

                val speedKM = (motion * 3.6).toFloat()

//                val a  = DecimalFormat("##.##").format(speedKM).toFloat()
//                val dT: Long = (event.timestamp ) / 1000000000.0f
                val dT: Long = (event.timestamp ) / 1000000L

                val timeInMillis: Long = (System.currentTimeMillis() + (event.timestamp - System.nanoTime()) / 1000000L)

                maxSpeed = max(maxSpeed , speedKM)
                sum += speedKM

                _currentSpeed.value = speedKM

                _maxSpeed.value =maxSpeed.toString()

                _average.value = (sum/count).toString()

//                val a  = (currentTimeMillis-dT)

                // set reference times
                if(sensorTimeReference == 0L && myTimeReference == 0L) {
                    sensorTimeReference = event.timestamp
                    myTimeReference = System.currentTimeMillis()
                }
                // set event timestamp to current time in milliseconds
//                val a  = ((event.timestamp - sensorTimeReference) / 1000000.0).roundToLong()
                val a =
                        Math.round((event.timestamp - sensorTimeReference) / 1000000.0)
                // some code...

                _time.value = getDate(a)

                _accuracy.value = event.accuracy.toString()

            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

//    fun getDate(timestamp: Long) :String {
//        val calendar = Calendar.getInstance(Locale.ENGLISH)
//        calendar.timeInMillis = timestamp * 1000L
//        val df = SimpleDateFormat("HH:mm", Locale.US).toString()
//        return df.format(calendar)
//    }

    fun getDate(timestamp: Long) :String {
//        val calendar = Calendar.getInstance()
//        val tz = TimeZone.getDefault()
//        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(timestamp)
    }
}