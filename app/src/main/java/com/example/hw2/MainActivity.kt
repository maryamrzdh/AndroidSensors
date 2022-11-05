package com.example.hw2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnSensor = findViewById<Button>(R.id.btn_sensors)

        btnSensor.setOnClickListener {
            startActivity(Intent(this,SensorActivity::class.java))
        }

    }
}