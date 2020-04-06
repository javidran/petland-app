package com.example.petland

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.parse.ParseUser
import java.text.SimpleDateFormat
import java.util.*

class UserProfileActivity : AppCompatActivity() {
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val buttonEditProfile : Button = findViewById(R.id.editProfile)
        buttonEditProfile.setOnClickListener { editProfile()}

        setUserInfo()
    }

    private fun editProfile() {
        val intent = Intent(this, EditProfileActivity::class.java).apply {}
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
}
