package com.example.petland.locations.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.R
import com.example.petland.locations.model.PetlandLocation
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {
    var location = PetlandLocation()
    private lateinit var layoutManager: LinearLayoutManager
    var reviewsList = listOf<ParseObject>() //Empty list of parse objects
    private lateinit var adapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(this)
        setContentView(R.layout.activity_review)

        recyclerView.layoutManager = layoutManager
        location = intent.extras?.get("Location") as PetlandLocation
        setUpValues()
    }

    override fun onResume() {
        super.onResume()
        startViewReview()
        setUpValues()
    }


    private fun setUpValues() {
        textName.text = location.getName()
        ratingBar.rating = location.getAverageStars().toFloat()
        ratingText.text = location.getAverageStars().toString()
        Log.d("ACTUALIZACION EN REVIEW",location.getAverageStars().toString() )
    }


    fun addReview(view: View) {
        val intent = Intent(this, AddReviewActivity::class.java).apply {}
        intent.putExtra("Location", location)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }
    fun returnLocation(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    fun startViewReview() {
        val query = ParseQuery.getQuery<ParseObject>("Review")
        query.whereEqualTo("location", location )
        reviewsList = query.find()
        if (reviewsList != null) {
            adapter = ReviewAdapter(reviewsList.toList())
            recyclerView.adapter = adapter
        }
    }

}
