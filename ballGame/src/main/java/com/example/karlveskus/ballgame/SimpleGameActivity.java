package com.example.karlveskus.ballgame;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.maryamrzdh.myapplication.R;

public class SimpleGameActivity extends AppCompatActivity implements SensorEventListener {

    private float xPos, xAcceleration, xVelocity = 0.0f;
    private float yPos, yVelocity = 0.0f;
    private float screenWidth;
    private float rotation = 0f;
    private SensorManager sensorManager;
    private ImageView ball;
    private LinearLayout ground;
    private LinearLayout obstacle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_game_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        ball = (ImageView) findViewById(R.id.ball);
        ground = (LinearLayout) findViewById(R.id.ground);
        obstacle = (LinearLayout) findViewById(R.id.obstacle);

        ConstraintLayout root = (ConstraintLayout) findViewById(R.id.main_layout);
        root.setOnTouchListener((new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (ballIsOnTheGround() || (ballIsOnTheObstacle())) {
                    yVelocity = -40f;
                }
                return true;
            }
        }));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = (float) displayMetrics.widthPixels;
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        updateXAccel(sensorEvent);
        updateX();
        updateY();

        ball.setX(xPos);
        ball.setY(yPos);

        rotation += xVelocity/2.5f;
        ball.setRotation(rotation);
    }

    void updateXAccel(SensorEvent sensorEvent) {
        xAcceleration = -sensorEvent.values[1];
    }

    void updateX() {
        xVelocity -= xAcceleration * 0.3f;

        if (ballRightSideWithSpeed() > screenWidth) {
            xPos = screenWidth - ball.getWidth();
            xVelocity = 0;
        } else if (ballLeftSideWithSpeed() < 0) {
            xPos = xVelocity = 0;
        } else if (ballIsHittingTheObstacleFromLeft() && ballBottomSide() > obstacleTopSide()) {
            xPos = obstacleLeftSide() - ball.getWidth();
            xVelocity = 0;
        } else if (ballIsHittingTheObstacleFromRight() && ballBottomSide() > obstacleTopSide()) {
            xPos = obstacleRightSide();
            xVelocity = 0;
        } else {
            xPos += xVelocity;
        }
    }

    void updateY() {
        if (ballIsHittingTheGround()) {
            yPos = ground.getY() - ball.getHeight();
            yVelocity = 0;
        } else if (ballIsHittingTheObstacleFromTop() &&
                ballRightSideWithSpeed() > obstacleLeftSide() &&
                ballLeftSideWithSpeed() < obstacleRightSide()) {
            yPos = obstacleTopSide() - ball.getHeight();
            yVelocity = 0;
        } else {
            yPos += yVelocity;
            yVelocity += 2f;
        }
    }

    private float obstacleTopSide() {
        return obstacle.getY();
    }

    private float obstacleRightSide() {
        return obstacle.getX() + obstacle.getWidth();
    }

    private float obstacleLeftSide() {
        return obstacle.getX();
    }


    private float ballRightSide() {
        return xPos + ball.getWidth();
    }

    private float ballBottomSide() {
        return ball.getY() + ball.getHeight();
    }


    private float ballRightSideWithSpeed() {
        return ballRightSide() + xVelocity;
    }

    private float ballLeftSideWithSpeed() {
        return xPos + xVelocity;
    }

    private float ballBottomSideWithSpeed() {
        return ballBottomSide() + yVelocity;
    }


    private boolean ballIsOnTheGround() {
        return ballBottomSide() == ground.getY();
    }

    private boolean ballIsOnTheObstacle() {
        return ballIsHittingTheObstacleFromTop() &&
                (ballIsHittingTheObstacleFromLeft() || ballIsHittingTheObstacleFromRight());
    }


    private boolean ballIsHittingTheGround() {
        return ballBottomSideWithSpeed() > ground.getY();
    }

    private boolean ballIsHittingTheObstacleFromTop() {
        return ballBottomSide() <= obstacleTopSide() &&
                ballBottomSideWithSpeed() >= obstacleTopSide();
    }

    private boolean ballIsHittingTheObstacleFromLeft() {
        return ballRightSideWithSpeed() > obstacleLeftSide() &&
                ballLeftSideWithSpeed() < obstacleLeftSide();
    }

    private boolean ballIsHittingTheObstacleFromRight() {
        return ballLeftSideWithSpeed() < obstacleRightSide() &&
                ballRightSideWithSpeed() > obstacleRightSide();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}