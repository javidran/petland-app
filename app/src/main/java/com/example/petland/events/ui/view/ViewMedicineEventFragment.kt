package com.example.petland.events.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.MedicineEvent
import kotlinx.android.synthetic.main.fragment_view_medicine_event.view.*

class ViewMedicineEventFragment : Fragment() {
    private lateinit var event: MedicineEvent
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
        rootView = inflater.inflate(R.layout.fragment_view_medicine_event, container, false)

        return rootView
    }

    override fun onResume() {
        super.onResume()

        rootView.viewMedicineName.text = event.getName()
        rootView.viewMedicineDosage.text = event.getDosage().toString()

        if(event.hasInfo()) {
            rootView.viewMedicineInfoLayout.visibility = View.VISIBLE
            rootView.viewMedicineInfo.text = event.getInfo()
        } else {
            rootView.viewMedicineInfoLayout.visibility = View.GONE
        }
    }

    companion object {
        private const val ARG_EVENT_DATA = "arg_event_data"

        @JvmStatic
        fun newInstance(event: MedicineEvent) =
            ViewMedicineEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EVENT_DATA, event)
                }
            }
    }
}
