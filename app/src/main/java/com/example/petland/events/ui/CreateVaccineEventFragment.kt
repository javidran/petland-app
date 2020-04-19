package com.example.petland.events.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.petland.R
import com.example.petland.events.model.VaccineEvent
import java.lang.NullPointerException

class CreateVaccineEventFragment : Fragment() {
    private lateinit var dataEvent: VaccineEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataEvent = it.getParcelable(DATA_EVENT) ?: throw NullPointerException("Debe recibir parametros")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_vaccine_event, container, false)
    }

    companion object {
        private const val TAG = "Petland Events"
        const val DATA_EVENT = "data_event"

        @JvmStatic
        fun newInstance(dataEvent: VaccineEvent) =
            CreateVaccineEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DATA_EVENT, dataEvent)
                }
            }
    }
}
