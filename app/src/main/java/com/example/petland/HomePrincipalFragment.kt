package com.example.petland

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.image.ImageUtils
import com.example.petland.mapas.MapsFragment
import com.example.petland.pet.Pets.Companion.getSelectedPet
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.fragment_home_principal.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.profileImage
import java.text.SimpleDateFormat
import java.util.*

class HomePrincipalFragment : Fragment() {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var rootView: View
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
        rootView.iniciarPaseoImage.setOnClickListener { iniciarPaseo() }

        return rootView
    }

    override fun onResume() {
        super.onResume()
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
}
