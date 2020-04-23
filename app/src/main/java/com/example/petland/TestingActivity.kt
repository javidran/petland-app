package com.example.petland

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.pet.ViewPetProfileActivity
import com.example.petland.sign.BootActivity
import com.example.petland.user_profile.invitations.ViewInvitationsActivity
import com.parse.ParseUser


class TestingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)
    }

    fun logOut(view: View) {
        //Paso view porque se llama desde el boton (en acivity_menu.xml)
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser != null) {
            Log.d(TAG, getString(R.string.loggedOut)) //Mensaje en logcat
            ParseUser.logOut()
            Toast.makeText(this, getString(R.string.loggedOut), Toast.LENGTH_SHORT).show()
            val intent = Intent(
                this,
                BootActivity::class.java
            ).apply { //Para pasar de esta vista, de nuevo al SignIn
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    fun goToPetProfile(view: View) {
        val intent = Intent(this, ViewPetProfileActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    fun goToViewInvitations(view: View) {
        val intent = Intent(this, ViewInvitationsActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    companion object {
        private const val TAG = "Petland Dashboard"
    }

}