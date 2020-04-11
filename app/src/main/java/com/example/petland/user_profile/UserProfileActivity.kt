package com.example.petland.user_profile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.ImageActivity
import com.example.petland.R
import com.example.petland.pet_creation.AddPetActivity
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.text.SimpleDateFormat
import java.util.*

class UserProfileActivity : AppCompatActivity() {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    lateinit var layoutManager : LinearLayoutManager
    lateinit var adapter : PetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        recyclerView.isNestedScrollingEnabled = false;
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        editProfileButton.setOnClickListener { editProfile() }
        addPetButton.setOnClickListener { addPet() }
        profileImage.setOnClickListener { seeImage() }
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
        updatePets()
    }

    private fun editProfile() {
        val intent = Intent(this, EditProfileActivity::class.java).apply {}
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    private fun seeImage() {
        val intent = Intent(this, ImageActivity::class.java).apply {}
        startActivity(intent)
    }

    private fun addPet() {
        val intent = Intent(this, AddPetActivity::class.java).apply {}
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun setUserInfo() {
        val user = ParseUser.getCurrentUser()

        val usernameText : TextView = findViewById(R.id.usernameText)
        usernameText.text = user.username

        val birthDayText : TextView = findViewById(R.id.birthdayText)
        birthDayText.text = sdf.format(user.get("birthday"))

        val nameText : TextView = findViewById(R.id.nameText)
        nameText.text = user.get("name").toString()

        val emailText : TextView = findViewById(R.id.emailText)
        emailText.text = user.email
    }

    private fun updatePets() {
        val pets = Pets()
        val petlist = pets.getPets()
        if(petlist != null) {
            adapter = PetAdapter(petlist.toList())
            recyclerView.adapter = adapter
        }
    }

}
