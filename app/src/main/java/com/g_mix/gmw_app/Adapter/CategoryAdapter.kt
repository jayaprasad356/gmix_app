package com.g_mix.gmw_app.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.Model.CategoryItem
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.activity.ProductDetailsActivity

class CategoryAdapter(private val categories: List<CategoryItem>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.tvTitle)
        private val categoryImage: ImageView = itemView.findViewById(R.id.ivItemImage)
        private val btnBuy: LinearLayout = itemView.findViewById(R.id.btnBuy)

        fun bind(category: CategoryItem) {
            categoryName.text = category.name
            category.image?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.demo_image)
                    .into(categoryImage)
            }

            // Handle click event for buy button
            btnBuy.setOnClickListener {
                val intent = Intent(itemView.context, ProductDetailsActivity::class.java).apply {
                    putExtra("PRODUCT_ID", category.id)
                    putExtra("ITEM_NAME", category.name)
                    putExtra("ITEM_PRICE", category.price)
                    putExtra("ITEM_IMAGE", category.image)
                    putExtra("ITEM_WEIGHT", category.measurement + " " + category.unit)
                    putExtra("ITEM_DESCRIPTION", category.description)
                    putExtra("ITEM_RATINGS", category.ratings)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}

