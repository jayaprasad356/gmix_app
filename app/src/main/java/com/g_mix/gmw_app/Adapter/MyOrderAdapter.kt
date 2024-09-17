package com.g_mix.gmw_app.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.g_mix.gmw_app.activity.LiveTrackingActivity
import com.g_mix.gmw_app.Model.OrderData
import com.g_mix.gmw_app.R

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

        viewHolder.tvProductName.text = "Product name: " + order.product_name + "(" + order.measurement +" "+ order.unit+" )"
        viewHolder.tvOrderId.text = "Order id #" + order.id.toString()
        viewHolder.tvPlacedDate.text = "Estimate deliver date : " + order.est_delivery_date
        viewHolder.tvQuantityVal.text = "${order.quantity}"  // Replace with actual quantity if available
        viewHolder.tvPrice.text = "₹ " + order.total_price
//        viewHolder.tvDeliveryDate.text = order.est_delivery_date
        viewHolder.tvDeliveryStatus.text = order.status

        val statusColor = Color.parseColor(order.status_color ?: "#000000") // Default to black if null
        viewHolder.tvDeliveryStatus.setTextColor(statusColor)
        viewHolder.ivDeliveryStatus.setColorFilter(statusColor)

        viewHolder.tvLiveTracking.setOnClickListener {
            val trackingUrl = order.live_tracking // Get the actual tracking URL
            val intent = Intent(context, LiveTrackingActivity::class.java)
            intent.putExtra("TRACKING_URL", trackingUrl) // Pass the URL to the activity
            context.startActivity(intent)
        }





//        if (order.status == "0") {
//            viewHolder.ivDeliveryStatus.setColorFilter(ContextCompat.getColor(context, R.color.text_grey_light))
//
//            viewHolder.tvDeliveryStatus.setTextColor(ContextCompat.getColor(context, R.color.text_grey_light))
//
//        } else if (order.status == "1") {
//            viewHolder.ivDeliveryStatus.setColorFilter(ContextCompat.getColor(context, R.color.darkGreen))
//            viewHolder.tvDeliveryStatus.text = "Order Placed"
//            viewHolder.tvDeliveryStatus.setTextColor(ContextCompat.getColor(context, R.color.darkGreen))
//            viewHolder.tvDeliveryDate.text = order.ordered_date
//
//        }

        return view
    }

    private class ViewHolder(view: View) {
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvPlacedDate: TextView = view.findViewById(R.id.tvPlacedDate)
        val tvQuantityVal: TextView = view.findViewById(R.id.tvQuantityVal)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvLiveTracking: TextView = view.findViewById(R.id.tvLiveTracking)
        val ivDeliveryStatus: ImageView = view.findViewById(R.id.ivDeliveryStatus)
        val tvDeliveryStatus: TextView = view.findViewById(R.id.tvDeliveryStatus)
    }
}