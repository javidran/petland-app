package com.example.petland.pet.creation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.pet.Pets
import com.example.petland.sign.BootActivity
import com.example.petland.user_profile.invitations.ViewInvitationsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.parse.ParseUser

class GetFirstPetActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_first_pet)
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    override fun onResume() {
        super.onResume()
        if(Pets.userHasPets()){
            val intent = Intent(this, HomeActivity::class.java).apply {}
            startActivity(intent)
            finish()
        }
    }

    fun createFirstPet(view: View) {
        val intent = Intent(this, AddPetActivity::class.java).apply {
        }
        startActivity(intent)
        finish()
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }


    fun acceptInvitation(view: View) {
        val intent = Intent(this, ViewInvitationsActivity::class.java).apply {
        }
        startActivity(intent)
        finish()
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    fun logOut(view: View){
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    Log.d("Petland GetFirstPet", getString(R.string.loggedOut)) //Mensaje en logcat
                    ParseUser.logOut()
                    Toast.makeText(this, getString(R.string.loggedOut), Toast.LENGTH_SHORT).show()
                    val intent = Intent(
                        this,
                        BootActivity::class.java
                    ).apply { //Para pasar de esta vista, de nuevo al SignIn
                    }
                    startActivity(intent)
                    finish()
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
                startActivity(Intent(this@GetFirstPetActivity, BootActivity::class.java))
                finish()
            }
    }

}