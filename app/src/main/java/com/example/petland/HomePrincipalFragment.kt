package com.example.petland

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.EventAdapter
import com.example.petland.events.ui.ViewEventActivity
import com.example.petland.events.ui.ViewEventCallback
import com.example.petland.events.ui.creation.CreateEventActivity
import com.example.petland.image.ImageUtils
import com.example.petland.pet.Pets.Companion.getSelectedPet
import kotlinx.android.synthetic.main.fragment_home_principal.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.profileImage
import java.text.SimpleDateFormat
import java.util.*

class HomePrincipalFragment : Fragment(), ViewEventCallback {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var rootView: View
    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       rootView = inflater.inflate(R.layout.fragment_home_principal, container, false)
        rootView.recyclerViewEvents.layoutManager = LinearLayoutManager(context)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        adapter = EventAdapter(PetEvent.getEventsFromPet(),  context!!, this)
        rootView.recyclerViewEvents.adapter = adapter
        setPetInfo()
    }
    private fun setPetInfo() {
       val pet = getSelectedPet()

        val petNameText: TextView = rootView.findViewById(R.id.petName)
        petNameText.text = pet.get("name").toString()

        val birthDayText: TextView = rootView.findViewById(R.id.birthday)
        birthDayText.text = sdf.format(pet.get("birthday"))


        ImageUtils.retrieveImage(pet, rootView.profileImage)
    }


    companion object {

        @JvmStatic
        fun newInstance() =
           HomePrincipalFragment().apply {}
    }

    override fun startViewEventActivity(event: PetEvent) {
        val intent = Intent(context, ViewEventActivity::class.java).apply {}
        intent.putExtra("event", event)
        startActivity(intent)
    }
}
