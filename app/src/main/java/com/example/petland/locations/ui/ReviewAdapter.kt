package com.example.petland.locations.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.parse.ParseObject
import com.parse.ParseUser
import kotlinx.android.synthetic.main.review_element.view.*

class ReviewAdapter(
    private val reviews: List<ParseObject>) :
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

        fun bindReviewInfo(review: ParseObject) {
            val creator = review.get("user") as ParseObject
            creator.fetch<ParseUser>()
            val nameCreator = creator.getString("name")
            val rate = review.getDouble("stars")
            val textComment = review.getString("text")
            view.user.text = nameCreator
            view.comment.text = textComment
            view.ratingBar.rating = rate.toFloat()
        }

    }

}