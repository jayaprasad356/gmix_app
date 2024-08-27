package com.g_mix.app.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.g_mix.app.Activity.AddressDetailActivity
import com.g_mix.app.Model.HomeProduct
import com.g_mix.app.R

class HomeAdapter(private val context: Context, private val items: List<HomeProduct>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_home_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val product = items[position]

        // Set the product details to the views
        viewHolder.title.text = product.name
        viewHolder.price.text = "₹ " + product.price
        viewHolder.weight.text = product.measurement + " " + product.unit

        // Load the image using Glide or any other image loading library
        Glide.with(context)
            .load(product.image)
            .placeholder(R.drawable.demo_image)
            .into(viewHolder.image)

        viewHolder.btnBuy.setOnClickListener {
            val intent = Intent(context, AddressDetailActivity::class.java).apply {
                putExtra("id", product.id)
                putExtra("ITEM_NAME", product.name)
                putExtra("ITEM_PRICE", product.price)
                putExtra("ITEM_IMAGE", product.image)
                putExtra("ITEM_WEIGHT", product.measurement + " " + product.unit)
            }
            context.startActivity(intent)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val title: TextView = view.findViewById(R.id.tvTitle)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val weight: TextView = view.findViewById(R.id.tvKilogram)
        val image: ImageView = view.findViewById(R.id.ivItemImage)
        val btnBuy: LinearLayout = view.findViewById(R.id.btnBuy)
    }

    companion object
}
