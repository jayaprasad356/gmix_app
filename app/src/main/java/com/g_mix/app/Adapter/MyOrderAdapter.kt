package com.g_mix.app.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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

    @SuppressLint("SetTextI18n")
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

        viewHolder.tvProductName.text = "Product name: " + order.product_name
        viewHolder.tvOrderId.text = "Order id #" + order.id.toString()
        viewHolder.tvPlacedDate.text = "Placed on " + order.created_at
        viewHolder.tvQuantityVal.text = "10"  // Replace with actual quantity if available
        viewHolder.tvPrice.text = "₹" + order.price
        viewHolder.tvDeliveryDate.text = order.delivery_date

        if (order.place_status == "0") {
            viewHolder.ivDeliveryStatus.setColorFilter(ContextCompat.getColor(context, R.color.text_grey_light))
            viewHolder.tvDeliveryStatus.text = "Order Delivered"
            viewHolder.tvDeliveryStatus.setTextColor(ContextCompat.getColor(context, R.color.text_grey_light))
        } else if (order.place_status == "1") {
            viewHolder.ivDeliveryStatus.setColorFilter(ContextCompat.getColor(context, R.color.darkGreen))
            viewHolder.tvDeliveryStatus.text = "Order Placed"
            viewHolder.tvDeliveryStatus.setTextColor(ContextCompat.getColor(context, R.color.darkGreen))
        }

        return view
    }

    private class ViewHolder(view: View) {
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvPlacedDate: TextView = view.findViewById(R.id.tvPlacedDate)
        val tvQuantityVal: TextView = view.findViewById(R.id.tvQuantityVal)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvDeliveryDate: TextView = view.findViewById(R.id.tvDeliveryDate)
        val ivDeliveryStatus: ImageView = view.findViewById(R.id.ivDeliveryStatus)
        val tvDeliveryStatus: TextView = view.findViewById(R.id.tvDeliveryStatus)
    }
}