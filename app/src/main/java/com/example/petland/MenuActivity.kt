package com.example.petland

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class MenuActivity : AppCompatActivity(){
   //  var Toolbar = toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
       // Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(Toolbar)
    }
}