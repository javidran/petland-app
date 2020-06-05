package com.example.petland.events.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.R
import com.example.petland.events.enums.FilterEvent
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.callback.ViewEventCallback
import com.example.petland.events.ui.creation.CreateEventActivity
import com.example.petland.events.ui.view.ViewEventActivity
import kotlinx.android.synthetic.main.fragment_events.view.*

class EventsFragment : Fragment(), ViewEventCallback, AdapterView.OnItemSelectedListener {
    private lateinit var adapter: EventAdapter
    private lateinit var rootView: View
    private var filter = FilterEvent.NEWEST_FIRST

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_events, container, false)
        rootView.floatingActionButton.setOnClickListener { createEvent() }
        rootView.recyclerViewEvents.layoutManager = LinearLayoutManager(context)

        val con: Context = context ?: throw NullPointerException()
        val adapterPet = ArrayAdapter(con, android.R.layout.simple_spinner_item, getFilters())
        adapterPet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView.filterSpinner.adapter = adapterPet
        rootView.filterSpinner.onItemSelectedListener = this

        return rootView
    }

    override fun onResume() {
        super.onResume()
        adapter = EventAdapter(PetEvent.getEventsFromPet(filter), requireContext(), this)
        rootView.recyclerViewEvents.adapter = adapter
    }

    private fun getFilters() : Array<String?> {
        val array = arrayOfNulls<String>(FilterEvent.values().size)
        FilterEvent.values().forEachIndexed { it, el ->
            when (el) {
                FilterEvent.NEWEST_FIRST -> array[it] = getString(R.string.newest_first)
                FilterEvent.OLDEST_FIRST -> array[it] = getString(R.string.oldest_first)
                FilterEvent.ONLY_FOOD -> array[it] = getString(R.string.food)
                FilterEvent.ONLY_HYGIENE -> array[it] = getString(R.string.hygiene)
                FilterEvent.ONLY_MEASUREMENT -> array[it] = getString(R.string.measurement)
                FilterEvent.ONLY_MEDICINE -> array[it] = getString(R.string.medicine)
                FilterEvent.ONLY_VACCINE -> array[it] = getString(R.string.vaccine)
                FilterEvent.ONLY_WALK -> array[it] = getString(R.string.walk)
            }
        }
        return array
    }

    private fun getFilterToEnum(value: String) : FilterEvent {
        when (value) {
            getString(R.string.newest_first) -> return FilterEvent.NEWEST_FIRST
            getString(R.string.oldest_first) -> return FilterEvent.OLDEST_FIRST
            getString(R.string.food) -> return FilterEvent.ONLY_FOOD
            getString(R.string.hygiene) -> return FilterEvent.ONLY_HYGIENE
            getString(R.string.measurement) -> return FilterEvent.ONLY_MEASUREMENT
            getString(R.string.medicine) -> return FilterEvent.ONLY_MEDICINE
            getString(R.string.vaccine) -> return FilterEvent.ONLY_VACCINE
            getString(R.string.walk) -> return FilterEvent.ONLY_WALK
        }
        return FilterEvent.NEWEST_FIRST //Default
    }

    private fun createEvent() {
        val intent = Intent(context, CreateEventActivity::class.java).apply {}
        startActivity(intent)
    }

    override fun startViewEventActivity(event: PetEvent) {
        val intent = Intent(context, ViewEventActivity::class.java).apply {}
        intent.putExtra("event", event)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "Petland Events"

        @JvmStatic
        fun newInstance() =
            EventsFragment().apply {}
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        filter = getFilterToEnum(parent?.getItemAtPosition(position).toString())
        adapter = EventAdapter(PetEvent.getEventsFromPet(filter), requireContext(), this)
        rootView.recyclerViewEvents.adapter = adapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

}
