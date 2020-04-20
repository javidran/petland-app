package com.example.petland.events.ui.creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.petland.R
import com.example.petland.events.model.MeasurementEvent
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_create_measurement_event.view.*
import kotlinx.android.synthetic.main.fragment_create_vaccine_event.view.*
import kotlinx.android.synthetic.main.fragment_create_vaccine_event.view.editHeight

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
        val view = inflater.inflate(R.layout.fragment_create_measurement_event, container, false)

        rootView = view

        return view
    }

    override fun checkAndSaveData(): ParseObject? {
        if(rootView.editHeight.text.isEmpty()) {
            rootView.editHeight.error = "Hight needed"
            return null
        }
        else {
            if(rootView.editWeight.text.isEmpty()) {
                rootView.editWeight.error = "Weight needed"
                return null
            }
            else {
                dataEvent.setHeight(rootView.editHeight.text.toString().toDouble())
                dataEvent.setWeight(rootView.editWeight.text.toString().toDouble())

                dataEvent.saveEvent()
                return dataEvent
            }
        }
    }

    companion object {
        private const val TAG = "Petland Events"
        const val DATA_EVENT = "data_event"

        @JvmStatic
        fun newInstance() =
            CreateMeasurementEventFragment().apply {}
    }


}
