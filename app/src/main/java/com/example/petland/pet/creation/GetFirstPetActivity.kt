package com.example.petland.pet.creation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.sign.BootActivity
import com.parse.ParseUser

class GetFirstPetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_first_pet)
    }

    fun createFirstPet(view: View) {
        val intent = Intent(this, AddPetActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }


    fun acceptInvitation(view: View) {
        val intent = Intent(this, HomeActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    fun logOut(view: View){
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser != null) {
            ParseUser.logOut()
            Toast.makeText(this, getString(R.string.loggedOut), Toast.LENGTH_SHORT).show()
            val intent = Intent(
                this,
                BootActivity::class.java
            ).apply { //Para pasar de esta vista, de nuevo al SignIn
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
    }

}