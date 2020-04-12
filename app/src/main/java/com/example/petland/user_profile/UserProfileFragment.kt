package com.example.petland.user_profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.R
import com.example.petland.image.ImageActivity
import com.example.petland.image.ImageUtils
import com.example.petland.pet.Pets
import com.example.petland.pet.creation.AddPetActivity
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import java.text.SimpleDateFormat
import java.util.*

class UserProfileFragment : Fragment() {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        view.editProfileButton.setOnClickListener { editProfile() }
        view.addPetButton.setOnClickListener { addPet() }
        view.profileImage.setOnClickListener { seeImage() }

        view.recyclerView.isNestedScrollingEnabled = false
        view.recyclerView.layoutManager = layoutManager

        setUserInfo(view)
        updatePets(view)

        return view
    }

    private fun editProfile() {
        val intent = Intent(context, EditProfileActivity::class.java).apply {}
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun seeImage() {
        val intent = Intent(context, ImageActivity::class.java).apply {}
        val user = ParseUser.getCurrentUser()
        intent.putExtra("object", user)
        startActivity(intent)
    }

    private fun addPet() {
        val intent = Intent(context, AddPetActivity::class.java).apply {}
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun setUserInfo(view: View) {
        val user = ParseUser.getCurrentUser()

        val usernameText: TextView = view.findViewById(R.id.usernameText)
        usernameText.text = user.username

        val birthDayText: TextView = view.findViewById(R.id.birthdayText)
        birthDayText.text = sdf.format(user.get("birthday"))

        val nameText: TextView = view.findViewById(R.id.nameText)
        nameText.text = user.get("name").toString()

        val emailText: TextView = view.findViewById(R.id.emailText)
        emailText.text = user.email

        val imageUtils = ImageUtils()
        imageUtils.retrieveImage(user, view.profileImage)
    }

    private fun updatePets(view: View) {
        val pets = Pets()
        val petlist = pets.getPets()
        if (petlist != null) {
            adapter = PetAdapter(petlist.toList())
            view.recyclerView.adapter = adapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            UserProfileFragment().apply {}
    }

}
