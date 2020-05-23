package com.example.petland.locations.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.petland.R
import com.example.petland.locations.model.PetlandLocation
import com.parse.ParseObject
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setUpValues()
    }

    private fun setUpValues() {
        val location = intent.extras?.get("Location") as PetlandLocation
        textName.text = location.getName()
    }

    fun addReview( view: View) {
        val intent = Intent(this, AddReviewActivity::class.java).apply {}
        startActivity(intent)
        finish()
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
}
