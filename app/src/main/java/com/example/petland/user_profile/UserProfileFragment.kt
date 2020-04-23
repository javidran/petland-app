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
import com.example.petland.image.ResetImageCallback
import com.example.petland.pet.Pets
import com.example.petland.pet.ViewPetProfileActivity
import com.example.petland.pet.creation.AddPetActivity
import com.example.petland.user_profile.invitations.ViewInvitationsActivity
import com.parse.ParseObject
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import java.text.SimpleDateFormat
import java.util.*

class UserProfileFragment : Fragment(), ResetImageCallback, ViewPetCallback {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PetAdapter
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false)

        rootView.editProfileButton.setOnClickListener { editProfile() }
        rootView.addPetButton.setOnClickListener { addPet() }
        rootView.reviewInvitationsButton.setOnClickListener { reviewInvitations() }
        rootView.profileImage.setOnClickListener { seeImage() }

        rootView.recyclerView.isNestedScrollingEnabled = false
        rootView.recyclerView.layoutManager = layoutManager

        return rootView
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
        updatePets()
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

    fun reviewInvitations() {
        val intent = Intent(context, ViewInvitationsActivity::class.java).apply {}
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun setUserInfo() {
        val user = ParseUser.getCurrentUser()

        val usernameText: TextView = rootView.findViewById(R.id.usernameText)
        usernameText.text = user.username

        val birthDayText: TextView = rootView.findViewById(R.id.birthdayText)
        birthDayText.text = sdf.format(user.get("birthday"))

        val nameText: TextView = rootView.findViewById(R.id.nameText)
        nameText.text = user.get("name").toString()

        val emailText: TextView = rootView.findViewById(R.id.emailText)
        emailText.text = user.email

        val imageUtils = ImageUtils()
        imageUtils.retrieveImage(user, rootView.profileImage, this)
    }

    private fun updatePets() {
        val petlist = Pets.getPetsFromCurrentUser()
        adapter = PetAdapter(petlist, this)
        rootView.recyclerView.adapter = adapter
    }

    override fun resetImage() {
        profileImage.setImageDrawable(context?.getDrawable(R.drawable.animal_paw))
    }

    override fun startViewPetActivity(pet: ParseObject) {
        val intent = Intent(context, ViewPetProfileActivity::class.java).apply {}
        intent.putExtra("petId", pet)
        intent.putExtra("eliminat", false)
        startActivity(intent);
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            UserProfileFragment().apply {}
    }

}
