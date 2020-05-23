package com.example.petland.health

import android.R.attr.label
import android.R.attr.onClick
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.events.enums.FilterEvent
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.EventAdapter
import com.example.petland.events.ui.callback.ViewEventCallback
import com.example.petland.events.ui.view.ViewEventActivity
import com.example.petland.pet.Pets
import kotlinx.android.synthetic.main.fragment_events.view.*
import kotlinx.android.synthetic.main.fragment_events.view.recyclerViewEvents
import kotlinx.android.synthetic.main.fragment_health.view.*
import kotlinx.android.synthetic.main.fragment_home_principal.view.*


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
        rootView.vetNum.setOnClickListener { copiarTelefono() }

        setData();
        return rootView;
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        val pet = Pets.getSelectedPet()
        val veterinary = Pets.getVeterinary(pet)
        if (veterinary != null) {
            val vetNum: Button = rootView.findViewById(R.id.vetNum)
            val infoVet: TextView = rootView.findViewById(R.id.infoVet)
            vetNum.text = veterinary.getNumber("phone_number").toString()
            infoVet.text = veterinary.getString("address")
        }
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

    private fun copiarTelefono() {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("NumVet", rootView.vetNum.text)
        clipboard.setPrimaryClip(clip)
    }
}
