package com.example.petland.health

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.petland.R
import com.example.petland.events.enums.FilterEvent
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.EventAdapter
import com.example.petland.events.ui.callback.ViewEventCallback
import com.example.petland.events.ui.view.ViewEventActivity
import com.example.petland.mapas.TimelineAdapter
import kotlinx.android.synthetic.main.fragment_events.view.*
import kotlinx.android.synthetic.main.fragment_timeline.view.*

class HealthFragment : Fragment(), ViewEventCallback {

    private lateinit var recyclerView: RecyclerView

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: EventAdapter
    private lateinit var rootView: View
    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health, container, false)
        rootView.recyclerViewEvents.isNestedScrollingEnabled = false
        rootView.recyclerViewEvents.layoutManager = layoutManager
        setData();
        return rootView;
    }

    override fun onResume() {
        super.onResume()
        adapter = EventAdapter(PetEvent.getEventsFromPet(FilterEvent.ONLY_VACCINE), requireContext(), this)
        rootView.recyclerViewEvents.adapter = adapter
    }

    private fun setData() {
        this.adapter = EventAdapter(PetEvent.getEventsFromPet(FilterEvent.ONLY_VACCINE), requireContext(), this)
        rootView.recyclerViewEvents.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HealthFragment().apply {}
    }

    override fun startViewEventActivity(event: PetEvent) {
        val intent = Intent(context, ViewEventActivity::class.java).apply {}
        intent.putExtra("event", event)
        startActivity(intent)
    }
}
