package com.example.petland

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class MenuActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

    }
    fun EditProfile(view: View) {
        val intent = Intent(this, EditProfileActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}