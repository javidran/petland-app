package com.example.petland.events.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.petland.R
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.creation.CreateEventActivity
import com.example.petland.pet.Pets
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.fragment_events.view.*
import java.lang.NullPointerException
import java.util.*

class EventsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_events, container, false)

        view.button4.setOnClickListener { createEvent() }

        return view
    }

    fun doThings() {
        val petclass = Pets()
        val pet :ParseObject = (petclass.getPets()?.get(0) ?: NullPointerException()) as ParseObject

        var event = PetEvent()

        event.setPet(pet)
        event.setDate(Calendar.getInstance().time)
        event.setRecurrent(2)

        event.saveEvent()
        Log.d("Petland Events", "Event saved")
    }

    fun markAsDone() {
        val petclass = Pets()
        val pet :ParseObject = (petclass.getPets()?.get(0) ?: NullPointerException()) as ParseObject

        val query = ParseQuery.getQuery(PetEvent::class.java)
        query.whereEqualTo("pet", pet)
        val event : PetEvent = (query.find()[0] ?: NullPointerException()) as PetEvent

        event.markAsDone(Calendar.getInstance().time)
    }

    fun createEvent() {
        val petclass = Pets()
        val pet :ParseObject = (petclass.getPets()?.get(0) ?: NullPointerException()) as ParseObject
        //TODO: Make pet changeable
        val intent = Intent(context, CreateEventActivity::class.java).apply {}
        intent.putExtra("petId", pet)
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
