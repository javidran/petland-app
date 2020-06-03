package com.example.petland.locations.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.petland.R
import com.example.petland.locations.model.PetlandLocation
import com.example.petland.locations.model.PetlandReview
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
        val reviews = PetlandReview.getReviewsUserInLocation(cUser, location)
        if (reviews.isNotEmpty()) {
             textTitle.text = getString(R.string.modify_review)
             textComment.text = getString(R.string.modify_comment)
             //MENSAJE  editar review en vista
             val textV = reviews[0].getText()
             stars = reviews[0].getStars().toDouble()
             textReview.setText(textV)
             ratingBar.rating = stars.toFloat()
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
        val reviews = PetlandReview.getReviewsUserInLocation(cUser, location)
        if (reviews.isNotEmpty()) {
            reviews[0].setText(textReview.text.toString())
            reviews[0].setStars(ratingBar.rating)
            reviews[0].setDate(today)
            reviews[0].saveReview()
            location.modifyStars(ratingBar.rating.toDouble(), stars)
            location.save()
        }
         else {
            val review = PetlandReview()
            review.setText(textReview.text.toString())
            review.setStars(ratingBar.rating)
            review.setDate(today)
            review.setUser(cUser)
            review.setLocation(location)
            review.saveReview()
            location.addStars(ratingBar.rating.toDouble())
            location.save()
        }
        finish()
    }
}
