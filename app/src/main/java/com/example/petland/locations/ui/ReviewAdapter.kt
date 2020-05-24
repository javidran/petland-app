package com.example.petland.locations.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.parse.ParseObject
import kotlinx.android.synthetic.main.review_element.view.*

class ReviewAdapter(
    private val reviews: List<ParseObject>,
    private val viewReviewsCallback: ViewReviewsCallback
) :
    RecyclerView.Adapter<ReviewAdapter.ReviewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ReviewHolder {
        return ReviewHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.user_invitation_element, p0, false),
            viewReviewsCallback
        )
    }

    override fun getItemCount(): Int {
        return reviews.count()
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bindReviewInfo(reviews[position])
    }

    class ReviewHolder(v: View, viewReviewsCallback: ViewReviewsCallback) :
        RecyclerView.ViewHolder(v) {
        var view: View = v
        val listCallback: ViewReviewsCallback = viewReviewsCallback

        private lateinit var review: ParseObject

        fun bindReviewInfo(review: ParseObject) {
            val creator = review.get("user") as ParseObject
            val rate = review.get("stars")
            val textComment = review.get("text")
            view.title.text = creator.toString()
            view.comment.text = textComment.toString()
            listCallback.startViewReview()

        }

    }

}