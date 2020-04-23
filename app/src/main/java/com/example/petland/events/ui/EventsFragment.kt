package com.example.petland.events.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.creation.CreateEventActivity
import com.example.petland.pet.Pets
import com.example.petland.user_profile.PetAdapter
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.fragment_events.view.*
import java.util.*

class EventsFragment : Fragment() {
    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events, container, false)

        view.floatingActionButton.setOnClickListener { createEvent() }

        view.recyclerViewEvents.layoutManager = LinearLayoutManager(context)
        adapter = EventAdapter(PetEvent.getEventsFromPet(), context!!)
        view.recyclerViewEvents.adapter = adapter

        return view
    }

    fun markAsDone() {
        val pet :ParseObject = Pets.getPetsFromCurrentUser()[0]

        val query = ParseQuery.getQuery(PetEvent::class.java)
        query.whereEqualTo("pet", pet)
        val event : PetEvent = (query.find()[0] ?: NullPointerException()) as PetEvent

        event.markAsDone(Calendar.getInstance().time)
    }

    private fun createEvent() {
        val intent = Intent(context, CreateEventActivity::class.java).apply {}
        startActivity(intent)
    }

    companion object {
        private const val TAG = "Petland Events"

        @JvmStatic
        fun newInstance() =
            EventsFragment().apply {
            }
    }
}
