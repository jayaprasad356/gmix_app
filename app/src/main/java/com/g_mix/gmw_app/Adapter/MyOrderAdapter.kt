package com.g_mix.gmw_app.Adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.g_mix.gmw_app.Fargment.ReviewFragment
import com.g_mix.gmw_app.Model.OrderData
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.activity.LiveTrackingActivity
import com.g_mix.gmw_app.helper.ApiConfig
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class MyOrderAdapter(
    private val activity: Activity,
    private val fragmentManager: androidx.fragment.app.FragmentManager,
    private var orderData: MutableList<OrderData>, // Change to MutableList
    private val onOrderSelected: (OrderData) -> Unit
) : RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder>() {

    private val session: Session = Session(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_my_product, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderData[position]
        holder.tvProductName.text = "Product name: ${order.product_name} (${order.measurement} ${order.unit})"
        holder.tvOrderId.text = "Order id #${order.id}"
        holder.tvPlacedDate.text = "Estimate deliver date: ${order.est_delivery_date}"
        holder.tvQuantityVal.text = "${order.quantity}"
        holder.tvPrice.text = "₹ ${order.total_price}"
        holder.tvDeliveryStatus.text = order.status
        holder.ratingBar.rating = order.ratings!!.toFloat()

        if(order.status == "Wait For Confirmation"){
            holder.ratingBar.visibility = View.GONE
            holder.tvReview.visibility = View.GONE
        }

        val statusColor = Color.parseColor(order.status_color ?: "#000000") // Default to black if null
        holder.tvDeliveryStatus.setTextColor(statusColor)
        holder.ivDeliveryStatus.setColorFilter(statusColor)

        holder.tvLiveTracking.setOnClickListener {
            val trackingUrl = order.live_tracking // Get the actual tracking URL
            val intent = Intent(activity, LiveTrackingActivity::class.java)
            intent.putExtra("TRACKING_URL", trackingUrl) // Pass the URL to the activity
            activity.startActivity(intent)
        }

        holder.tvReview.setOnClickListener {
            val transaction = fragmentManager.beginTransaction()

            // Create an instance of the fragment
            val reviewFragment = ReviewFragment()

            // Create a Bundle to pass data
            val bundle = Bundle()
            bundle.putString("order_id", order.id.toString()) // Pass the order ID or any other data

            // Set the bundle to the fragment
            reviewFragment.arguments = bundle

            // Replace the fragment and add to back stack
            transaction.replace(R.id.fragment_container, reviewFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        holder.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            val ratingValue = rating.toInt()

            rating(ratingValue, order.id)

        }

    }

    override fun getItemCount(): Int {
        return orderData.size
    }

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvPlacedDate: TextView = view.findViewById(R.id.tvPlacedDate)
        val tvQuantityVal: TextView = view.findViewById(R.id.tvQuantityVal)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvLiveTracking: TextView = view.findViewById(R.id.tvLiveTracking)
        val ivDeliveryStatus: ImageView = view.findViewById(R.id.ivDeliveryStatus)
        val tvDeliveryStatus: TextView = view.findViewById(R.id.tvDeliveryStatus)
        val tvReview: TextView = view.findViewById(R.id.tvReview)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
    }


    private fun rating(ratingValue: Int, id: Int?) {
        val params: MutableMap<String, String> = HashMap()
        params[Constant.USER_ID] = session.getData(Constant.USER_ID)
        params[Constant.ORDER_ID] = id.toString()
        params[Constant.RATINGS] = ratingValue.toString()
        activity?.let {
            ApiConfig.RequestToVolley({ result, response ->
                if (result) {
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.getBoolean(Constant.SUCCESS)) {
                            Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show()
                            // Handle error
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }, it, Constant.UPDATE_RATINGS, params, true)
        }
    }




}
