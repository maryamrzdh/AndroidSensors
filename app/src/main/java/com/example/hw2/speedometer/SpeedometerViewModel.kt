package com.example.hw2.speedometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.SystemClock
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class SpeedometerViewModel:ViewModel() ,SensorEventListener {

    private var sum = 0f
    private var count = 0

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

    private var _distance = MutableStateFlow("")
    var distance = _distance.asStateFlow()

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

//                val timeInMillis: Long = (System.currentTimeMillis() + (event.timestamp - System.nanoTime()) / 1000000L)


                maxSpeed = max(maxSpeed , speedKM)
                sum += speedKM

                _currentSpeed.value = speedKM

                _maxSpeed.value =maxSpeed.toString()

                _average.value = (sum/count).toString()

                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
                val millis = System.currentTimeMillis() + (event.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000L

                _time.value = printDifference( sdf.parse(sdf.format(Date(currentTimeMillis))),sdf.parse(sdf.format(Date(millis))))

                _accuracy.value = event.accuracy.toString()

                _distance.value = "0"
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


    private fun printDifference(startDate: Date, endDate: Date):String {
        //milliseconds
        var different = endDate.time - startDate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        System.out.printf(
            "%d days, %d hours, %d minutes, %d seconds%n",
            elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
        )
        return "$elapsedHours : $elapsedMinutes : $elapsedSeconds"
    }
}