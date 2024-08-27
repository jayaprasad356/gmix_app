package com.g_mix.app.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.g_mix.app.Model.OrderData
import com.g_mix.app.R

class MyOrderAdapter(private val context: Context, private val orderData: ArrayList<OrderData>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return orderData.size
    }

    override fun getItem(position: Int): Any {
        return orderData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_my_product, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val order = getItem(position) as OrderData
        Log.d("OrderAdapter", "Binding Order: ${order.product_name}")  // Log each item being bound

        viewHolder.tvTitle.text = order.product_name
        viewHolder.tvKilogram.text = order.address_name
        viewHolder.tvPlacedDate.text = order.created_at
        viewHolder.tvQuantityVal.text = "10"  // Replace with actual quantity if available
        viewHolder.tvPrice.text = order.price

        Glide.with(context)
            .load(order.delivery_charges)  // Make sure this is a valid URL or resource
            .placeholder(R.drawable.order_box_im)
            .into(viewHolder.ivItemImage)

        return view
    }


    private class ViewHolder(view: View) {
        val ivItemImage: ImageView = view.findViewById(R.id.ivItemImage)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvKilogram: TextView = view.findViewById(R.id.tvKilogram)
        val tvPlacedDate: TextView = view.findViewById(R.id.tvPlacedDate)
        val tvQuantityVal: TextView = view.findViewById(R.id.tvQuantityVal)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
    }
}