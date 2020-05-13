package com.example.petland.events.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.MeasurementEvent
import kotlinx.android.synthetic.main.fragment_view_measurement_event.view.*

class ViewMeasurementEventFragment : Fragment() {
    private lateinit var event: MeasurementEvent
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            event = it.getParcelable(ARG_EVENT_DATA)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_view_measurement_event, container, false)

        return rootView
    }

    override fun onResume() {
        super.onResume()

        if(event.hasHeight()) {
            rootView.viewMeasurementHeightLayout.visibility = View.VISIBLE
            rootView.viewMeasurementHeight.text = event.getHeight().toString()
        } else {
            rootView.viewMeasurementHeightLayout.visibility = View.GONE
        }

        if(event.hasWeight()) {
            rootView.viewMeasurementWeightLayout.visibility = View.VISIBLE
            rootView.viewMeasurementWeight.text = event.getWeight().toString()
        } else {
            rootView.viewMeasurementWeightLayout.visibility = View.GONE
        }
    }

    companion object {
        private const val ARG_EVENT_DATA = "arg_event_data"

        @JvmStatic
        fun newInstance(event: MeasurementEvent) =
            ViewMeasurementEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EVENT_DATA, event)
                }
            }
    }
}
