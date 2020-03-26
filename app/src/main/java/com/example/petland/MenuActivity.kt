package com.example.petland

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser


class MenuActivity : AppCompatActivity(){
   //  var Toolbar = toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
       // Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(Toolbar)
    }

    fun logOut(view: View){//Paso view porque se llama desde el botno (en acivity_menu.xml)
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser != null) {
            Log.d("LogOut","Log Out Correcto")//Mensaje en logcat
            ParseUser.logOut()
            Toast.makeText(this,"Logged out",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java).apply {//Para pasar de esta vista, de nuevo al SignIn
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            //val currentUser = ParseUser.getCurrentUser() // this will now be null
        }
    }
}