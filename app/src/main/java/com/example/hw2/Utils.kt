package com.example.hw2

import android.content.Context
import android.hardware.Sensor

fun Sensor.getSensorName(ctx:Context):String{
    return when(this.type){
        Sensor.TYPE_GYROSCOPE ->  ctx.resources.getString(R.string.Gyroscope)
        Sensor.TYPE_ACCELEROMETER ->  ctx.resources.getString(R.string.Acceleration)
        Sensor.TYPE_MAGNETIC_FIELD ->  ctx.resources.getString(R.string.Magnetic)
        Sensor.TYPE_LIGHT ->  ctx.resources.getString(R.string.light)
        Sensor.TYPE_ORIENTATION ->  ctx.resources.getString(R.string.Orientation)
        Sensor.TYPE_AMBIENT_TEMPERATURE ->  ctx.resources.getString(R.string.thermometer)
        Sensor.TYPE_PROXIMITY ->  ctx.resources.getString(R.string.Proximity)
        else ->this.name
    }
}

fun Sensor.getSensorDesc(ctx:Context):String{
    return when(this.type){
        Sensor.TYPE_GYROSCOPE ->  ctx.resources.getString(R.string.Gyroscope_desc)
        Sensor.TYPE_ACCELEROMETER ->  ctx.resources.getString(R.string.Acceleration_desc)
        Sensor.TYPE_MAGNETIC_FIELD ->  ctx.resources.getString(R.string.Magnetic_desc)
        Sensor.TYPE_LIGHT ->  ctx.resources.getString(R.string.light_desc)
        Sensor.TYPE_ORIENTATION ->  ctx.resources.getString(R.string.Orientation_desc)
        Sensor.TYPE_AMBIENT_TEMPERATURE ->  ctx.resources.getString(R.string.thermometer_desc)
        Sensor.TYPE_PROXIMITY ->  ctx.resources.getString(R.string.Proximity_desc)
        else ->this.name
    }
}