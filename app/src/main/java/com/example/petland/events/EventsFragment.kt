package com.example.petland.events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.petland.R
import com.example.petland.pet.Pets
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_events.view.*
import java.lang.NullPointerException
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [EventsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_events, container, false)

        view.button2.setOnClickListener { doThings() }
        view.button3.setOnClickListener { markAsDone() }


        return view
    }

    fun doThings() {
        val petclass = Pets()
        val pet :ParseObject = (petclass.getPets()?.get(0) ?: NullPointerException()) as ParseObject

        var event = PetCareDataEntity()

        event.setPet(pet)
        event.setDate(Calendar.getInstance().time)
        event.setRecurrent(2)

        event.saveEvent()
        Log.d("Petland Events", "Event saved")
    }

    fun markAsDone() {
        val petclass = Pets()
        val pet :ParseObject = (petclass.getPets()?.get(0) ?: NullPointerException()) as ParseObject

        val query = ParseQuery.getQuery(PetCareDataEntity::class.java)
        query.whereEqualTo("pet", pet)
        var event : PetCareDataEntity = (query.find()[0] ?: NullPointerException()) as PetCareDataEntity

        event.markAsDone(Calendar.getInstance().time)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment EventsFragment.
         */
        @JvmStatic
        fun newInstance() =
            EventsFragment().apply {
            }
    }
}
