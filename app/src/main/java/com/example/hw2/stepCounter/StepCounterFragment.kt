package com.example.hw2.stepCounter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.hw2.BaseFragment
import com.example.hw2.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StepCounterFragment : BaseFragment() {

    private lateinit var btnEnd : Button
    private lateinit var btnStart : Button
    private lateinit var tvStepsTaken: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step_counter, container, false)

        btnStart = view.findViewById(R.id.btn_start)
        btnEnd = view.findViewById(R.id.btn_end)
        tvStepsTaken = view.findViewById(R.id.tv_stepsTaken)

        btnStart.setOnClickListener {

            btnStart.visibility = View.GONE
            btnEnd.visibility = View.VISIBLE
            EventBus.getDefault().register(this)
            startService()

        }
        resetSteps()

        return view
    }

    private fun resetSteps() {
        btnEnd.setOnClickListener {
            Toast.makeText(requireContext(), "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        btnEnd.setOnLongClickListener {
            btnEnd.visibility = View.GONE
            btnStart.visibility = View.VISIBLE

            // When the user will click long tap on the screen,
            // the steps will be reset to 0
            tvStepsTaken.text = 0.toString()

            EventBus.getDefault().unregister(this)
            stopMyService()

            true
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: StepEvent?) {
        tvStepsTaken.text = event?.value.toString()
    }

    private fun startService() {
        val intentService = Intent(requireContext() , StepCounterService::class.java)
        requireActivity().startService(intentService)
    }

    private fun stopMyService() {
        val serviceIntent = Intent(requireActivity(), StepCounterService::class.java)
        requireActivity().stopService(serviceIntent)
    }

    override fun onDetach() {
        super.onDetach()
        stopMyService()
    }

}