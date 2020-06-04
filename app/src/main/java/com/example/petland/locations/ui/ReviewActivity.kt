package com.example.petland.locations.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.R
import com.example.petland.locations.model.PetlandLocation
import com.example.petland.locations.model.PetlandReview
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {
    var location = PetlandLocation()
    private lateinit var layoutManager: LinearLayoutManager
    var reviewsList = listOf<PetlandReview>() //Empty list of parse objects
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
        location.fetch<PetlandLocation>()
        textName.text = location.getName()
        ratingBar.rating = location.getAverageStars().toFloat()
        ratingText.text = String.format("%.2f", location.getAverageStars())
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
        reviewsList = PetlandReview.getReviewsLocation(location)
        adapter = ReviewAdapter(reviewsList.toList())
        recyclerView.adapter = adapter
    }

}
