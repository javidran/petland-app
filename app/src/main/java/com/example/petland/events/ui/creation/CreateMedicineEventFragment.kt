package com.example.petland.events.ui.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.MedicineEvent
import com.example.petland.events.ui.callback.SaveDataCallback
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_create_medicine_event.view.*

class CreateMedicineEventFragment : Fragment(),
    SaveDataCallback {
    private var dataEvent = MedicineEvent()
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_create_medicine_event, container, false)
        return rootView
    }

    override fun checkAndSaveData(): ParseObject? {
        if(rootView.editMedicineName.text.isEmpty()) {
            rootView.editMedicineName.error = getString(R.string.date_needed)
            return null
        }
        else {
            if(rootView.editMedicineDosage.text.isEmpty()) {
                rootView.editMedicineDosage.error = getString(R.string.content_mandatory)
                return null
            }
            else{
                dataEvent.setName(rootView.editMedicineName.text.toString())
                dataEvent.setDosage(rootView.editMedicineDosage.text.toString().toInt())
                if(rootView.editMedicineInfo.text.isNotEmpty()) {
                    dataEvent.setInfo(rootView.editMedicineInfo.text.toString())
                }
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
            CreateMedicineEventFragment().apply {
            }
    }


}
