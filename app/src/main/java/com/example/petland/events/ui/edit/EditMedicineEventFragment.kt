package com.example.petland.events.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.MedicineEvent
import com.example.petland.events.ui.callback.SaveDataCallback
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_edit_medicine_event.view.*

class EditMedicineEventFragment : Fragment(),
    SaveDataCallback {
    private var dataEvent = MedicineEvent()
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
        rootView = inflater.inflate(R.layout.fragment_edit_medicine_event, container, false)

        rootView.editMedicineName.setText(dataEvent.getName())
        rootView.editMedicineDosage.setText(dataEvent.getDosage().toString())
        if(dataEvent.hasInfo()) {
            rootView.editMedicineInfo.setText(dataEvent.getInfo())
        }

        return rootView
    }

    override fun checkAndSaveData(): ParseObject? {
        if(rootView.editMedicineName.text.isEmpty()) {
            rootView.editMedicineName.error = getString(R.string.needed)
            return null
        }
        else {
            if(rootView.editMedicineDosage.text.isEmpty()) {
                rootView.editMedicineDosage.error = getString(R.string.needed)
                return null
            }
            else{
                dataEvent.setName(rootView.editMedicineName.text.toString())
                dataEvent.setDosage(rootView.editMedicineDosage.text.toString().toInt())
                if(rootView.editMedicineInfo.text.isNotEmpty()) {
                    dataEvent.setInfo(rootView.editMedicineInfo.text.toString())
                } else {
                    dataEvent.removeInfo()
                }
                dataEvent.saveEvent()
                return dataEvent
            }
        }

    }

    companion object {
        private const val TAG = "Petland Events"
        private const val ARG_PARAM = "arg_param"
        const val DATA_EVENT = "data_event"

        @JvmStatic
        fun newInstance(dataEvent: MedicineEvent) =
            EditMedicineEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM, dataEvent)
                }
            }
    }


}
