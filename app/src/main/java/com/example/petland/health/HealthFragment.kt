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
import android.widget.ImageButton
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
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_events.view.*
import kotlinx.android.synthetic.main.fragment_events.view.recyclerViewEvents
import kotlinx.android.synthetic.main.fragment_health.view.*
import kotlinx.android.synthetic.main.fragment_home_principal.view.*


class HealthFragment : Fragment(), ViewEventCallback {

    private lateinit var recyclerView: RecyclerView

    private lateinit var layoutManagerVac: LinearLayoutManager
    private lateinit var layoutManagerMed: LinearLayoutManager
    private lateinit var adapter: EventAdapter
    private lateinit var rootView: View
    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManagerVac = LinearLayoutManager(context)
        layoutManagerMed = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health, container, false)
        rootView.recyclerViewEventsVac.isNestedScrollingEnabled = false
        rootView.recyclerViewEventsVac.layoutManager = layoutManagerVac
        rootView.recyclerViewEventsMed.isNestedScrollingEnabled = false
        rootView.recyclerViewEventsMed.layoutManager = layoutManagerMed
        rootView.vetNum.setOnClickListener { copiarTelefono() }

        createFragment();
        return rootView;
    }

    override fun onResume() {
        super.onResume()
        createFragment()
    }

    private fun createFragment() {
        val pet = Pets.getSelectedPet()
        val veterinary = Pets.getVeterinary(pet)

        if (veterinary != null) hasVet(veterinary)
        else visibilities(false)

        this.adapter = EventAdapter(PetEvent.getEventsFromPetNotDone(FilterEvent.ONLY_VACCINE), requireContext(), this)
        rootView.recyclerViewEventsVac.adapter = adapter

        this.adapter = EventAdapter(PetEvent.getEventsFromPetNotDone(FilterEvent.ONLY_MEDICINE), requireContext(), this)
        rootView.recyclerViewEventsMed.adapter = adapter
    }

    private fun hasVet(veterinaty: ParseObject) {
        visibilities(true)

        val vetNum: Button = rootView.findViewById(R.id.vetNum)
        val vetInfo: TextView = rootView.findViewById(R.id.infoVet)
        val vetNom: TextView = rootView.findViewById(R.id.nomVet)

        vetNum.text = veterinaty.getNumber("phone_number").toString()
        vetInfo.text = veterinaty.getString("address")
        vetNom.text = veterinaty.getString("name")
    }

    private fun visibilities(hasVet : Boolean) {
        val newVet: TextView = rootView.findViewById(R.id.newVet)
        val newVetIcon: ImageButton = rootView.findViewById(R.id.newVetIcon)
        val vetNum: Button = rootView.findViewById(R.id.vetNum)
        val vetInfo: TextView = rootView.findViewById(R.id.infoVet)
        val vetNom: TextView = rootView.findViewById(R.id.nomVet)
        val searchVet: TextView = rootView.findViewById(R.id.searchVet)
        val searchVetIcon: ImageButton = rootView.findViewById(R.id.searchVetIcon)

        if(hasVet) {
            newVet.visibility = View.VISIBLE
            newVetIcon.visibility = View.VISIBLE
            vetNum.visibility = View.VISIBLE
            vetInfo.visibility = View.VISIBLE
            vetNom.visibility = View.VISIBLE

            searchVet.visibility = View.INVISIBLE
            searchVetIcon.visibility = View.INVISIBLE

        }
        else {
            searchVet.visibility = View.VISIBLE
            searchVetIcon.visibility = View.VISIBLE

            newVet.visibility = View.INVISIBLE
            newVetIcon.visibility = View.INVISIBLE
            vetNum.visibility = View.INVISIBLE
            vetInfo.visibility = View.INVISIBLE
            vetNom.visibility = View.INVISIBLE
        }
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
