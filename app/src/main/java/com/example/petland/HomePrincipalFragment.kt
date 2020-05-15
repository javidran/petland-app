package com.example.petland

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.example.petland.pet.ViewPetProfileActivity
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_home_principal.*
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

    private var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(activity!!)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       rootView = inflater.inflate(R.layout.fragment_home_principal, container, false)
        rootView.viewProfileButton.setOnClickListener { viewProfileActivity(getSelectedPet()) }
        rootView.recyclerViewEvents.layoutManager = LinearLayoutManager(context)
        rootView.iniciarPaseoImage.setOnClickListener { iniciarPaseo() }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        val listAdapter = PetEvent.getEventsWithoutWalk(getSelectedPet())
        visibleNoEvents = listAdapter.isEmpty()
        adapter = EventAdapter(listAdapter,  requireContext(), this) //Android Studio ME HA PEDIDO QUE LO CAMBIE A requiereContext()
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

        val textWalkCard: Button = rootView.findViewById(R.id.textWalkCard)
        textWalkCard.text = PetEvent.getNextWalkEventDate(getSelectedPet())

        if(visibleNoEvents) {
            eventsHome.text = getString(R.string.noEvents)

        }
        else {
            eventsHome.text = getString(R.string.events_header)
        }


        ImageUtils.retrieveImage(pet, rootView.profileImage)
    }

 fun viewProfileActivity(pet: ParseObject)  {
        val intent = Intent(context, ViewPetProfileActivity::class.java).apply {}
        intent.putExtra("petId", pet)
        intent.putExtra("eliminat", false)
        startActivity(intent);
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
