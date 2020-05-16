package com.example.petland.events.ui.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.MeasurementEvent
import com.example.petland.events.ui.callback.SaveDataCallback
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_create_measurement_event.view.*

class CreateMeasurementEventFragment : Fragment(),
    SaveDataCallback {
    private var dataEvent = MeasurementEvent()
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_create_measurement_event, container, false)
        return rootView
    }

    override fun checkAndSaveData(): ParseObject? {
        if(rootView.editMeasurementHeight.text.isNotEmpty()) {
            dataEvent.setHeight(rootView.editMeasurementHeight.text.toString().toDouble())
        }
        if(rootView.editMeasurementWeight.text.isNotEmpty()) {
            dataEvent.setWeight(rootView.editMeasurementWeight.text.toString().toDouble())
        }
        dataEvent.saveEvent()
        return dataEvent

    }

    companion object {
        private const val TAG = "Petland Events"
        const val DATA_EVENT = "data_event"

        @JvmStatic
        fun newInstance() =
            CreateMeasurementEventFragment().apply {}
    }


}
