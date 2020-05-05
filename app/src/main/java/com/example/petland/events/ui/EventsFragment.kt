package com.example.petland.events.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.R
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.creation.CreateEventActivity
import kotlinx.android.synthetic.main.fragment_events.view.*

class EventsFragment : Fragment(), ViewEventCallback {
    private lateinit var adapter: EventAdapter
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_events, container, false)
        rootView.floatingActionButton.setOnClickListener { createEvent() }
        rootView.recyclerViewEvents.layoutManager = LinearLayoutManager(context)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        adapter = EventAdapter(PetEvent.getEventsFromPet(), context!!, this)
        rootView.recyclerViewEvents.adapter = adapter
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
}
