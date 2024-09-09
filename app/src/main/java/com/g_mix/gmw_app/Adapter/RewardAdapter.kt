package com.g_mix.gmw_app.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.Model.Reward
import com.g_mix.gmw_app.R
import com.g_mix.gmw_app.helper.Constant
import com.g_mix.gmw_app.helper.Session

class RewardAdapter(private val context: Context, private val reward: ArrayList<Reward>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return reward.size
    }

    override fun getItem(position: Int): Any {
        return reward[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_my_reward, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val reward = getItem(position) as Reward


        // Set the product details to the views
        viewHolder.title.text = reward.name
        viewHolder.price.text = "★" + reward.points + " Points"
    //    viewHolder.tvDiscription.text = reward.description

        // Load the image using Glide or any other image loading library
        Glide.with(context)
            .load(reward.image)
            .placeholder(R.drawable.demo_image)
            .into(viewHolder.image)

        viewHolder.btnBuy.setOnClickListener {
            val session = Session(context)
            val points = session.getData(Constant.POINTS)?.toIntOrNull() ?: 0  // Get user points
            val rewardPoints = reward.points?.toIntOrNull() ?: 0  // Get reward points

            if (points > rewardPoints || points == rewardPoints) {
                // If points are higher than reward points, redirect to WhatsApp with a pre-filled message
                val whatsappNumber = session.getData(Constant.CUSTOMER_SUPPORT_NUMBER)// Replace with your WhatsApp number
                val message = "I want to order the product ${reward.name}."
                val url = "https://wa.me/$whatsappNumber?text=${Uri.encode(message)}"

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            } else {
                // Show an alert dialog box for insufficient funds
                AlertDialog.Builder(context).apply {
                    setTitle("Insufficient Funds")
                    setMessage("You do not have enough points to redeem this reward.")
                    setPositiveButton("OK", null)
                    create()
                    show()
                }
            }

        }




        return view
    }

    private class ViewHolder(view: View) {
        val title: TextView = view.findViewById(R.id.tvTitle)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val tvDiscription: TextView = view.findViewById(R.id.tvDiscription)
        val image: ImageView = view.findViewById(R.id.ivItemImage)
        val btnBuy: LinearLayout = view.findViewById(R.id.btnBuy)
    }
}