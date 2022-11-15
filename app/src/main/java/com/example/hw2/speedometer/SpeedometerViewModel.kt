package com.example.hw2.speedometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class SpeedometerViewModel:ViewModel() ,SensorEventListener {

    private var sum = 0f
    private var count = 0
    private var mAccel: Float = 0f
    private var hourLeft = 0L
    private var _currentSpeed = MutableStateFlow(0f)
    var currentSpeed = _currentSpeed.asStateFlow()

    private var _maxSpeed = MutableSharedFlow<String>(replay = 1)
    var maxSpeedState = _maxSpeed.asSharedFlow()

    private var _average = MutableSharedFlow<String>(replay = 1)
    var average = _average.asSharedFlow()

    private var _time = MutableStateFlow("")
    var time = _time.asStateFlow()

    private var _accuracy = MutableSharedFlow<String>(replay = 1)
    var accuracy = _accuracy.asSharedFlow()

    private var _distance = MutableSharedFlow<String>(replay = 1)
    var distance = _distance.asSharedFlow()

    private var _lastTick = System.currentTimeMillis()
    private var currentTimeMillis = System.currentTimeMillis()
    private val _samplePeriod = 15
    private var maxSpeed = 0f

    override fun onSensorChanged(event: SensorEvent?) {

        count+=1

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

                maxSpeed = max(maxSpeed , speedKM)
                sum += speedKM

                _currentSpeed.value = speedKM

                val max =maxSpeed.toString()

                val avg = (sum/count).toString()

                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
                val millis = System.currentTimeMillis() + (event.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000L

                _time.value = printDifference( sdf.parse(sdf.format(Date(currentTimeMillis))),sdf.parse(sdf.format(Date(millis))))

                val acc = event.accuracy.toString()

                //              mAccel = mAccel * 0.9f + motion.toFloat() * 0.1f
                mAccel += ((sum/count) * hourLeft ) / 60

                viewModelScope.launch {
                    _accuracy.emit(acc)
                    _average.emit(avg)
                    _maxSpeed.emit(max)
                    _distance.emit("$mAccel km")
                }


            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

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
        hourLeft = (elapsedHours *60) + elapsedMinutes
        return "$elapsedHours : $elapsedMinutes : $elapsedSeconds"
    }
}