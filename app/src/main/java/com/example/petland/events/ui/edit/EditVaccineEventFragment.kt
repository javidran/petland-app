package com.example.petland.events.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.VaccineEvent
import com.example.petland.events.ui.callback.SaveDataCallback
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_edit_vaccine_event.view.*

class EditVaccineEventFragment : Fragment(),
    SaveDataCallback {
    private var dataEvent = VaccineEvent()
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
        rootView = inflater.inflate(R.layout.fragment_edit_vaccine_event, container, false)

        rootView.editVaccineName.setText(dataEvent.getName())

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
        private const val ARG_PARAM = "arg_param"
        const val DATA_EVENT = "data_event"

        @JvmStatic
        fun newInstance(dataEvent: VaccineEvent) =
            EditVaccineEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM, dataEvent)
                }
            }
    }


}
