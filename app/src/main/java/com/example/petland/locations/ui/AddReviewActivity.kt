package com.example.petland.locations.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.example.petland.R
import com.example.petland.locations.model.PetlandLocation
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_add_review.*
import java.util.*

class AddReviewActivity : AppCompatActivity() {
    var location = PetlandLocation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
    }

    fun checkReview(){
        val cUser = ParseUser.getCurrentUser()
        val query = ParseQuery.getQuery<ParseObject>("Review")
        query.whereEqualTo("user", cUser )
        query.whereEqualTo("place", location )
        query.findInBackground { reviews, e ->
            if (e == null) {
                if (reviews.size > 0) {
                    val textV = reviews[0].get("text")
                    val starsV = reviews[0].get("stars")
                    textReview.text = textV as Editable?
                    ratingBar.rating = starsV as Float
                }
            }
        }
    }

    fun saveReview(view: View){
        val cUser = ParseUser.getCurrentUser()
        val today = Calendar.getInstance().time
        val query = ParseQuery.getQuery<ParseObject>("Review")
        query.whereEqualTo("user", cUser )
        query.whereEqualTo("place", location )
        query.findInBackground { reviews, e ->
            if (e == null) {
                if (reviews.size > 0) {
                    reviews[0].put("text", textReview.text)
                    reviews[0].put("stars", ratingBar.rating)
                    reviews[0].put("date", today)
                    reviews[0].saveInBackground()
                }
                else {
                    val review = ParseObject("Review")
                    review.put("text", textReview.text)
                    review.put("stars", ratingBar.rating)
                    review.put("date", today)
                    review.put("user", cUser)
                    review.put("location", location)
                    review.saveInBackground()

                    val toast1 = Toast.makeText(
                        Parse.getApplicationContext(),
                        "Create review", Toast.LENGTH_SHORT
                    )
                    toast1.show()
                }
            }
        }
    }
}
