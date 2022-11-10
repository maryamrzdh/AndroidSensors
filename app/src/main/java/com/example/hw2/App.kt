package com.example.hw2

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App:Application() {

    var context : Context? = null

    override fun onCreate() {
        super.onCreate()

        context = this

        sharedPreferences = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()!!
    }

    companion object{

        private var sharedPreferences : SharedPreferences? = null
        lateinit var editor : SharedPreferences.Editor

        fun saveShay(key: String, value: Float) {
            editor.putFloat(key, value)
            editor.apply()
        }

        fun loadShay(key: String , setDefault: Float): Float {
            return sharedPreferences?.getFloat(key, setDefault) ?:0f
        }
    }
}