package com.example.petland.locations.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.example.petland.locations.model.PetlandReview
import com.parse.ParseUser
import kotlinx.android.synthetic.main.review_element.view.*
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter(
    private val reviews: List<PetlandReview>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ReviewHolder {
        return ReviewHolder(LayoutInflater.from(p0.context).inflate(R.layout.review_element, p0, false))
    }

    override fun getItemCount(): Int {
        return reviews.count()
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bindReviewInfo(reviews[position])
    }

    class ReviewHolder(v: View) :
        RecyclerView.ViewHolder(v) {
        var view: View = v

        fun bindReviewInfo(review: PetlandReview) {
            val sdf = SimpleDateFormat("dd/MM/yyyy  hh:mm aaa", Locale.US)
            val nameCreator = review.getUsername()
            val rate = review.getStars()
            val textComment = review.getText()
            view.user.text = nameCreator
            ImageUtils.retrieveImage(review.getUser(), view.userImage)
            view.date.text = sdf.format(review.getDate()).toString()
            view.comment.text = textComment
            view.ratingBar.rating = rate
        }

    }

}