package com.example.petland.events.ui.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.VaccineEvent
import com.example.petland.events.ui.callback.SaveDataCallback
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
        rootView = inflater.inflate(R.layout.fragment_create_vaccine_event, container, false)
        return rootView
    }

    override fun checkAndSaveData(): ParseObject? {
        if(rootView.editVaccineName.text.isEmpty()) {
            rootView.editVaccineName.error = getString(R.string.needed)
            return null
        }
        dataEvent.setName(rootView.editVaccineName.text.toString())
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
