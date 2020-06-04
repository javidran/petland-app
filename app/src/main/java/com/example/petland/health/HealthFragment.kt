package com.example.petland.health

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.events.enums.FilterEvent
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.EventAdapter
import com.example.petland.events.ui.callback.ViewEventCallback
import com.example.petland.events.ui.view.ViewEventActivity
import com.example.petland.locations.model.PetlandLocation
import com.example.petland.pet.Pet
import kotlinx.android.synthetic.main.fragment_health.view.*


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
        rootView.medicalHistory.setOnClickListener { seeMedicalHistory() }
        rootView.newVet.setOnClickListener { searchVeterinary() }
        rootView.searchVetIcon.setOnClickListener { searchVeterinary() }

        createFragment();
        return rootView;
    }

    override fun onResume() {
        super.onResume()
        createFragment()
    }

    private fun createFragment() {
        if (Pet.getSelectedPet().hasVeterinary()) {
            hasVet(Pet.getSelectedPet().getVeterinary())
        }
        else visibilities(false)

        this.adapter = EventAdapter(PetEvent.getEventsFromPetNotDone(FilterEvent.ONLY_VACCINE), requireContext(), this)
        rootView.recyclerViewEventsVac.adapter = adapter

        this.adapter = EventAdapter(PetEvent.getEventsFromPetNotDone(FilterEvent.ONLY_MEDICINE), requireContext(), this)
        rootView.recyclerViewEventsMed.adapter = adapter
    }

    private fun hasVet(veterinary: PetlandLocation) {
        visibilities(true)

        val vetNum: Button = rootView.findViewById(R.id.vetNum)
        val vetInfo: TextView = rootView.findViewById(R.id.infoVet)
        val vetNom: TextView = rootView.findViewById(R.id.nomVet)

        if (veterinary.hasPhoneNumber()) vetNum.text = veterinary.getPhoneNumber().toString()
        else vetNum.text = null
        vetInfo.text = veterinary.getAddress()
        vetNom.text = veterinary.getName()
    }

    private fun visibilities(hasVet: Boolean) {
        val newVet: Button = rootView.findViewById(R.id.newVet)
        val vetNum: Button = rootView.findViewById(R.id.vetNum)
        val vetInfo: TextView = rootView.findViewById(R.id.infoVet)
        val vetNom: TextView = rootView.findViewById(R.id.nomVet)
        val searchVet: TextView = rootView.findViewById(R.id.searchVet)
        val searchVetIcon: ImageButton = rootView.findViewById(R.id.searchVetIcon)

        if(hasVet) {
            newVet.visibility = View.VISIBLE
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
            vetNum.visibility = View.INVISIBLE
            vetInfo.visibility = View.INVISIBLE
            vetNom.visibility = View.INVISIBLE
        }
    }

    companion object {
        private const val CALL_PERMISSION_REQUEST_CODE = 1
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

        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.CALL_PHONE),
                CALL_PERMISSION_REQUEST_CODE)
            return
        }
        else {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + rootView.vetNum.text as String?)
            startActivity(intent)
        }
    }

    fun seeMedicalHistory() {
        val intent = Intent(context, MedicalHistoryActivity::class.java).apply {}
        startActivity(intent)
    }

    fun searchVeterinary() {
        (activity as HomeActivity).searchVeterinary()
    }
}
