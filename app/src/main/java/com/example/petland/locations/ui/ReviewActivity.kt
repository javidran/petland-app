package com.example.petland.locations.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.locations.model.PetlandLocation
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity(), ViewReviewsCallback {
    var location = PetlandLocation()
    private lateinit var layoutManager: LinearLayoutManager
    var reviewsList = listOf<ParseObject>() //Empty list of parse objects
    private lateinit var adapter: ReviewAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(this)
        setContentView(R.layout.activity_review)
        recyclerView.isNestedScrollingEnabled = false //evitar scrolling
        recyclerView.layoutManager = layoutManager
        setUpValues()
    }
    override fun onResume() {
        super.onResume()
        startViewReview()
    }

    private fun setUpValues() {
        location = intent.extras?.get("Location") as PetlandLocation
        textName.text = location.getName()
    }

    fun addReview( view: View) {
        val intent = Intent(this, AddReviewActivity::class.java).apply {}
        intent.putExtra("Location", location)
        startActivity(intent)
        finish()
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    override fun startViewReview() {
        // val cUser = ParseUser.getCurrentUser()
        val query = ParseQuery.getQuery<ParseObject>("Review")
        query.whereEqualTo("location", location )
        reviewsList = query.find()
        if (reviewsList != null) {
            adapter =
                ReviewAdapter(
                    reviewsList.toList(),
                    this
                )
            recyclerView.adapter = adapter

        }
    }

}
