package com.example.hw2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val permissionRequest = 0
//    private var storagePermitted = true

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // ask for permissions if haven't got one
        if (ContextCompat.checkSelfPermission(
                this, arrayOf(
                    Manifest.permission.ACTIVITY_RECOGNITION,
                ).toString()
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACTIVITY_RECOGNITION,
                ), permissionRequest
            )
        }
    }

    // if the permission is not granted
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionRequest -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
//
//                }else{
//
//                }

            }
        }
        return
    }

}