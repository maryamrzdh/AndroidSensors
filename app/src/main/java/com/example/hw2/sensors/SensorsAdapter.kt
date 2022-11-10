package com.example.hw2.sensors

import android.content.Context
import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.hw2.R
import com.example.hw2.getSensorName

class SensorsAdapter(
    val ctx:Context,
    private val sensors: List<Sensor>,
    private val onClick: (Sensor) -> Unit) :
    RecyclerView.Adapter<SensorsAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnSensor: Button

        init {
            // Define click listener for the ViewHolder's View.
            btnSensor = view.findViewById(R.id.btn_sensor)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.sensor_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val sensor = sensors[position]

        viewHolder.btnSensor.text = sensor.getSensorName(ctx)

        viewHolder.btnSensor.setOnClickListener {
            onClick(sensor)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = sensors.size

}