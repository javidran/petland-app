package com.example.petland.locations.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.petland.R
import com.example.petland.locations.model.PetlandLocation
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_add_review.*
import java.util.*

class AddReviewActivity : AppCompatActivity() {
    var location = PetlandLocation()
    var stars = 0.0
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
                    textTitle.text = "Modify your review:"
                    textComment.text = "Modify your comment: "
                    //MENSAJE  editar review en vista
                    val textV = reviews[0].getString("text")
                    stars = reviews[0].getDouble("stars")
                    Log.d("antiguas stars", stars.toString())
                    textReview.setText(textV)
                    ratingBar.rating = stars.toFloat()
                }
            }
        }
    }
    fun returnReviews(view: View){

        val builder = AlertDialog.Builder(this@AddReviewActivity)
        builder.setTitle(getString(R.string.notsaved))
        builder.setMessage(getString(R.string.dialogReview))
        builder.setPositiveButton((getString(R.string.ok))){dialog, which ->
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        builder.setNeutralButton(getString(R.string.cancel)){_,_ ->
        }
        val dialog: AlertDialog = builder.create()

        dialog.show()



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
                    location.addStars( stars + (ratingBar.rating.toDouble() - stars), true )

                }
                else {
                    val review = ParseObject("Review")
                    review.put("text", textReview.text.toString())
                    review.put("stars", ratingBar.rating.toDouble())
                    review.put("date", today)
                    review.put("user", cUser)
                    review.put("location", location)
                    review.save()
                    Log.d("estrellas" , ratingBar.rating.toDouble().toString())
                    location.addStars(ratingBar.rating.toDouble(), false)

                }

                finish()
            }
        }
    }
}
