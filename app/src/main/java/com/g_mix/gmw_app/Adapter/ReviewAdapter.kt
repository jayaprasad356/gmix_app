package com.g_mix.gmw_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.Model.Review
import com.g_mix.gmw_app.R

class ReviewAdapter(private val context: Context, private val reviewList: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    // ViewHolder class to represent each item in the RecyclerView
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvReview: TextView = itemView.findViewById(R.id.tvReview)
        val ivReview1: ImageView = itemView.findViewById(R.id.ivReview1)
        val ivReview2: ImageView = itemView.findViewById(R.id.ivReview2)
        val ivReview3: ImageView = itemView.findViewById(R.id.ivReview3)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        // Inflate the item layout for each item in the RecyclerView
        val view = LayoutInflater.from(context).inflate(R.layout.layout_review, parent, false)
        return ReviewViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        // Get the data model based on position
        val review = reviewList[position]

        // Set the review description
        holder.tvReview.text = review.description
        holder.tvRating.text = review.ratings.toString()

        // Load images using Glide and handle visibility when image URLs are null or empty
        if (!review.image1.isNullOrEmpty()) {
            holder.ivReview1.visibility = View.VISIBLE
            Glide.with(context).load(review.image1).into(holder.ivReview1)
        } else {
            holder.ivReview1.visibility = View.GONE
        }

        if (!review.image2.isNullOrEmpty()) {
            holder.ivReview2.visibility = View.VISIBLE
            Glide.with(context).load(review.image2).into(holder.ivReview2)
        } else {
            holder.ivReview2.visibility = View.GONE
        }

        if (!review.image3.isNullOrEmpty()) {
            holder.ivReview3.visibility = View.VISIBLE
            Glide.with(context).load(review.image3).into(holder.ivReview3)
        } else {
            holder.ivReview3.visibility = View.GONE
        }
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return reviewList.size
    }
}
