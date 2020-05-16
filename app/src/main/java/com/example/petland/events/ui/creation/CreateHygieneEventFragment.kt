package com.example.petland.events.ui.creation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.enums.HygieneType
import com.example.petland.events.model.HygieneEvent
import com.example.petland.events.ui.callback.SaveDataCallback
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_create_hygiene_event.view.*

class CreateHygieneEventFragment : Fragment(),
    SaveDataCallback {
    private var dataEvent = HygieneEvent()
    private lateinit var rootView: View
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_create_hygiene_event, container, false)

        spinner = rootView.hyigieneSpinner
        val con: Context = context ?: throw NullPointerException()
        val adapter = ArrayAdapter(
            con,
            android.R.layout.simple_spinner_item, getEventHygieneToString()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        return rootView
    }

    override fun checkAndSaveData(): ParseObject? {
        dataEvent.setType(getEventHygieneToEnum(spinner.selectedItem.toString()))
        dataEvent.saveEvent()
        return dataEvent
    }

    private fun getEventHygieneToString() : Array<String?> {
        val array = arrayOfNulls<String>(HygieneType.values().size)
        HygieneType.values().forEachIndexed { it, el ->
            when (el) {
                HygieneType.BATH -> array[it] = getString(R.string.hygiene_bath)
                HygieneType.DEWORM -> array[it] = getString(R.string.hygiene_deworm)
                HygieneType.HAIRCUT -> array[it] = getString(R.string.hygiene_haircut)
                HygieneType.NAILS -> array[it] = getString(R.string.hygiene_nails)
            }
        }
        return array
    }

    private fun getEventHygieneToEnum(value: String) : HygieneType {
        when (value) {
            getString(R.string.hygiene_bath) -> return HygieneType.BATH
            getString(R.string.hygiene_deworm) -> return HygieneType.DEWORM
            getString(R.string.hygiene_haircut) -> return HygieneType.HAIRCUT
            getString(R.string.hygiene_nails) -> return HygieneType.NAILS
        }
        return HygieneType.BATH //Default
    }

    companion object {
        private const val TAG = "Petland Events"
        const val DATA_EVENT = "data_event"

        @JvmStatic
        fun newInstance() =
            CreateHygieneEventFragment().apply {
            }
    }
}
