//package com.example.hw2.stepCounter
//
//import android.content.ComponentName
//import android.content.ServiceConnection
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.os.IBinder
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.hw2.App
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//
//
//class StepCounterViewModel : ViewModel(),SensorEventListener {
//
//    // Creating a variable which will counts total steps
//    // and it has been given the value of 0 float
//    var totalSteps = 0f
//
//    // Creating a variable  which counts previous total
//    // steps and it has also been given the value of 0 float
//    var previousTotalSteps = 0f
//
//    private var stepCounter = MutableStateFlow(0)
//    val stepCount = stepCounter.asStateFlow()
//
//    override fun onSensorChanged(event: SensorEvent?) {
//
////        if (running) {
//        totalSteps = event!!.values[0]
//
//        // Current steps are calculated by taking the difference of total steps
//        // and previous steps
//        stepCounter.value = totalSteps.toInt() - previousTotalSteps.toInt()
//    }
//
//    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
//
//
//     fun saveData() {
//
//        // Shared Preferences will allow us to save
//        // and retrieve data in the form of key,value pair.
//        // In this function we will save data
////        App.saveShay("key1" , previousTotalSteps)
//        App.saveShay("key1" , totalSteps)
//    }
//
//    fun loadData() {
//
//        // In this function we will retrieve data
//        val savedNumber = App.loadShay("key1",0f)
//
//        // Log.d is used for debugging purposes
//        Log.d("StepCounter", "$savedNumber")
//
//        previousTotalSteps = savedNumber
//    }
//
//    ////
//
//    private val TAG = "MainActivityViewModel"
//
//    private val mIsProgressBarUpdating = MutableLiveData<Boolean>()
//    private val mBinder: MutableLiveData<StepCounterService.MyBinder?> = MutableLiveData<StepCounterService.MyBinder?>()
//
//
//    // Keeping this in here because it doesn't require a context
//    private val serviceConnection: ServiceConnection = object : ServiceConnection {
//        override fun onServiceConnected(className: ComponentName?, iBinder: IBinder?) {
//            Log.d(TAG, "ServiceConnection: connected to service.")
//            // We've bound to MyService, cast the IBinder and get MyBinder instance
//            val binder: StepCounterService.MyBinder? = iBinder as StepCounterService.MyBinder?
//            mBinder.postValue(binder)
//        }
//
//        override fun onServiceDisconnected(arg0: ComponentName?) {
//            Log.d(TAG, "ServiceConnection: disconnected from service.")
//            mBinder.postValue(null)
//        }
//    }
//
//
//    fun getServiceConnection(): ServiceConnection? {
//        return serviceConnection
//    }
//
//    fun getBinder(): LiveData<StepCounterService.MyBinder?>? {
//        return mBinder
//    }
//
//
//    fun getIsProgressBarUpdating(): LiveData<Boolean>? {
//        return mIsProgressBarUpdating
//    }
//
//    fun setIsProgressBarUpdating(isUpdating: Boolean) {
//        mIsProgressBarUpdating.postValue(isUpdating)
//    }
//}