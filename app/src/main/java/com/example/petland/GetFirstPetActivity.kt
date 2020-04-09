package com.example.petland

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class GetFirstPetActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_first_pet)
    }

    fun createFirstPet(view: View) {
        val intent = Intent(this, AddPetActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    fun acceptInvitation(view: View) {
        val intent = Intent(this, MenuActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}