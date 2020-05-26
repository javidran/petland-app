package com.example.petland.locations.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.example.petland.R
import com.example.petland.locations.model.PetlandLocation
import com.example.petland.locations.model.PetlandReview
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_add_review.*
import java.util.*

class AddReviewActivity : AppCompatActivity() {
    var location = PetlandLocation()
    private var review  = PetlandReview()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
        location = intent.extras?.get("Location") as PetlandLocation
        checkReview()
    }

    fun checkReview() {
        val cUser = ParseUser.getCurrentUser()
        val query = ParseQuery.getQuery<ParseObject>("Review")
        query.whereEqualTo("user", cUser )
        query.whereEqualTo("location", location )
        query.findInBackground { reviews, e ->
            if (e == null) {
                if (reviews.size > 0) {
                    //MENSAJE  editar review en vista
                    val textV = reviews[0].getString("text")
                    val starsV = reviews[0].getDouble("stars")
                    textReview.setText(textV)
                    ratingBar.rating = starsV.toFloat()
                }
            }
        }
    }

    fun saveReview(view: View) {
        val cUser = ParseUser.getCurrentUser()
        val today = Calendar.getInstance().time
        val query = ParseQuery.getQuery<ParseObject>("Review")
        query.whereEqualTo("user", cUser )
        query.whereEqualTo("location", location)

        query.findInBackground { reviews, e ->
            if (e == null) {
                if (reviews.size > 0) {
                    reviews[0].put("text", textReview.text.toString())
                    reviews[0].put("stars", ratingBar.rating)
                    reviews[0].put("date", today)
                    reviews[0].saveInBackground()
                }
                else {
                    val review = ParseObject("Review")
                    review.put("text", textReview.text.toString())
                    review.put("stars", ratingBar.rating.toDouble())
                    review.put("date", today)
                    review.put("user", cUser)
                    review.put("location", location)
                    review.save()
                    location.addStars(ratingBar.rating.toDouble())

                }
                finish()
            }
        }
    }
}
