package com.example.hw2

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SimpleGameActivity : AppCompatActivity(), SensorEventListener {
    private var xPos = 0f
    private var xAcceleration = 0f
    private var xVelocity = 0.0f
    private var yPos = 0f
    private var yVelocity = 0.0f
    private var screenWidth = 0f
    private var rotationR = 0f
    private var sensorManager: SensorManager? = null
    private var ball: ImageView? = null
    private var ground: LinearLayout? = null
    private var obstacle: LinearLayout? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_game)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        ball = findViewById<View>(R.id.ball) as ImageView
        ground = findViewById<View>(R.id.ground) as LinearLayout
        obstacle = findViewById<View>(R.id.obstacle) as LinearLayout
        val root = findViewById<View>(R.id.main_layout) as ConstraintLayout
        root.setOnTouchListener { view, event ->
            if (ballIsOnTheGround() || ballIsOnTheObstacle()) {
                yVelocity = -40f
            }
            true
        }
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels.toFloat()
    }

    override fun onStart() {
        super.onStart()
        sensorManager!!.registerListener(
            this, sensorManager!!.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            ), SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onStop() {
        sensorManager!!.unregisterListener(this)
        super.onStop()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        updateXAccel(sensorEvent)
        updateX()
        updateY()

        rotationR += xVelocity / 2.5f

        ball?.apply {
            x = xPos
            y = yPos
            rotation = rotation
        }
        ball?.setX(xPos)
        ball?.setY(yPos)
        ball?.setRotation(rotationR)
    }

    fun updateXAccel(sensorEvent: SensorEvent) {
        xAcceleration = -sensorEvent.values[1]
    }

    fun updateX() {
        xVelocity -= xAcceleration * 0.3f
        if (ballRightSideWithSpeed() > screenWidth) {
            xPos = screenWidth - ball!!.width
            xVelocity = 0f
        } else if (ballLeftSideWithSpeed() < 0) {
            xVelocity = 0f
            xPos = xVelocity
        } else if (ballIsHittingTheObstacleFromLeft() && ballBottomSide() > obstacleTopSide()) {
            xPos = obstacleLeftSide() - ball!!.width
            xVelocity = 0f
        } else if (ballIsHittingTheObstacleFromRight() && ballBottomSide() > obstacleTopSide()) {
            xPos = obstacleRightSide()
            xVelocity = 0f
        } else {
            xPos += xVelocity
        }
    }

    fun updateY() {
        if (ballIsHittingTheGround()) {
            yPos = ground!!.y - ball!!.height
            yVelocity = 0f
        } else if (ballIsHittingTheObstacleFromTop() && ballRightSideWithSpeed() > obstacleLeftSide() && ballLeftSideWithSpeed() < obstacleRightSide()) {
            yPos = obstacleTopSide() - ball!!.height
            yVelocity = 0f
        } else {
            yPos += yVelocity
            yVelocity += 2f
        }
    }

    private fun obstacleTopSide(): Float {
        return obstacle!!.y
    }

    private fun obstacleRightSide(): Float {
        return obstacle!!.x + obstacle!!.width
    }

    private fun obstacleLeftSide(): Float {
        return obstacle!!.x
    }

    private fun ballRightSide(): Float {
        return xPos + ball!!.width
    }

    private fun ballBottomSide(): Float {
        return ball!!.y + ball!!.height
    }

    private fun ballRightSideWithSpeed(): Float {
        return ballRightSide() + xVelocity
    }

    private fun ballLeftSideWithSpeed(): Float {
        return xPos + xVelocity
    }

    private fun ballBottomSideWithSpeed(): Float {
        return ballBottomSide() + yVelocity
    }

    private fun ballIsOnTheGround(): Boolean {
        return ballBottomSide() == ground!!.y
    }

    private fun ballIsOnTheObstacle(): Boolean {
        return ballIsHittingTheObstacleFromTop() &&
                (ballIsHittingTheObstacleFromLeft() || ballIsHittingTheObstacleFromRight())
    }

    private fun ballIsHittingTheGround(): Boolean {
        return ballBottomSideWithSpeed() > ground!!.y
    }

    private fun ballIsHittingTheObstacleFromTop(): Boolean {
        return ballBottomSide() <= obstacleTopSide() &&
                ballBottomSideWithSpeed() >= obstacleTopSide()
    }

    private fun ballIsHittingTheObstacleFromLeft(): Boolean {
        return ballRightSideWithSpeed() > obstacleLeftSide() &&
                ballLeftSideWithSpeed() < obstacleLeftSide()
    }

    private fun ballIsHittingTheObstacleFromRight(): Boolean {
        return ballLeftSideWithSpeed() < obstacleRightSide() &&
                ballRightSideWithSpeed() > obstacleRightSide()
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}
}