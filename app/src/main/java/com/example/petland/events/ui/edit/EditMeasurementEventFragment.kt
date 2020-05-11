package com.example.petland.events.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.MeasurementEvent
import com.example.petland.events.ui.callback.SaveDataCallback
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_edit_measurement_event.view.*

class EditMeasurementEventFragment : Fragment(),
    SaveDataCallback {
    private var dataEvent = MeasurementEvent()
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataEvent = it.getParcelable(ARG_PARAM)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_edit_measurement_event, container, false)

        if(dataEvent.hasHeight()) {
            rootView.editMeasurementHeight.setText(dataEvent.getHeight().toString())
        }
        if(dataEvent.hasWeight()) {
            rootView.editMeasurementWeight.setText(dataEvent.getWeight().toString())
        }

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
        private const val ARG_PARAM = "arg_param"
        const val DATA_EVENT = "data_event"

        @JvmStatic
        fun newInstance(dataEvent: MeasurementEvent) =
            EditMeasurementEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM, dataEvent)
                }
            }
    }


}
