package com.example.petland

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.events.enums.FilterEvent
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.EventAdapter
import com.example.petland.events.ui.callback.ViewEventCallback
import com.example.petland.events.ui.view.ViewEventActivity
import com.example.petland.image.ImageUtils
import com.example.petland.mapas.MapsFragment
import com.example.petland.pet.Pets.Companion.getSelectedPet
import com.example.petland.user_profile.EditProfileActivity
import kotlinx.android.synthetic.main.fragment_home_principal.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.profileImage
import java.text.SimpleDateFormat
import java.util.*


class HomePrincipalFragment : Fragment(), ViewEventCallback {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var rootView: View
    private lateinit var adapter: EventAdapter
    private var visibleNoEvents: Boolean = false
    lateinit var fragment: Fragment

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
        rootView.editProfileButton.setOnClickListener { editProfileActivity() }
        rootView.recyclerViewEvents.layoutManager = LinearLayoutManager(context)
        rootView.iniciarPaseoImage.setOnClickListener { iniciarPaseo() }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        val listAdapter = PetEvent.getEventsWithoutWalk(getSelectedPet())
        visibleNoEvents = listAdapter.isEmpty()
        adapter = EventAdapter(listAdapter,  context!!, this)

        rootView.recyclerViewEvents.adapter = adapter
        PetEvent.getEventsFromPet(FilterEvent.ONLY_WALK)
        setPetInfo()
    }
    private fun setPetInfo() {
       val pet = getSelectedPet()

        val petNameText: TextView = rootView.findViewById(R.id.petName)
        petNameText.text = pet.get("name").toString()

        val birthDayText: TextView = rootView.findViewById(R.id.birthday)
        birthDayText.text = sdf.format(pet.get("birthday"))


        val textWalk: TextView = rootView.findViewById(R.id.textWalk)
        textWalk.text = PetEvent.getWalkEventDate(getSelectedPet())

        val textNoEvents: TextView = rootView.findViewById(R.id.textNoEvents)
        if(visibleNoEvents) {
            textNoEvents.visibility = View.VISIBLE
            textNoEvents.text = getString(R.string.noEvents)

        }
        else textNoEvents.visibility = View.INVISIBLE

        ImageUtils.retrieveImage(pet, rootView.profileImage)
    }

fun editProfileActivity() {
    val i = Intent(activity, EditProfileActivity::class.java)
    startActivity(i)
    (activity as Activity?)!!.overridePendingTransition(R.anim.slide_in_right,
        R.anim.slide_out_left)
}
 fun iniciarPaseo() {

     val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
     fragment = MapsFragment.newInstance()
     transaction.replace(R.id.frameLayout, fragment)
     transaction.commit()
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
