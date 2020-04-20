package com.example.petland.events.ui.creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.petland.R
import com.example.petland.events.model.VaccineEvent
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_create_vaccine_event.view.*

class CreateVaccineEventFragment : Fragment(),
    SaveDataCallback {
    private var dataEvent = VaccineEvent()
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_vaccine_event, container, false)

        rootView = view

        return view
    }

    override fun checkAndSaveData(): ParseObject? {
        if(rootView.editHeight.text.isEmpty()) {
            rootView.editHeight.error = "Date needed"
            return null
        }
        dataEvent.setName(rootView.editHeight.text.toString())
        dataEvent.saveEvent()
        return dataEvent
    }

    companion object {
        private const val TAG = "Petland Events"
        const val DATA_EVENT = "data_event"

        @JvmStatic
        fun newInstance() =
            CreateVaccineEventFragment().apply {
            }
    }


}
