package com.g_mix.gmw_app.Adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.Model.SliderItem
import com.g_mix.gmw_app.R

class SliderAdapter(private val items: List<SliderItem>) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = items[position]
        Log.d("SliderAdapter", "Binding item at position $position, isVideo: ${item.image}")

        holder.videoView.stopPlayback()
        holder.videoView.setVideoURI(null)

        holder.imageView.visibility = View.VISIBLE
        holder.videoView.visibility = View.GONE

        Glide.with(holder.imageView.context)
            .load(item.image)
            .into(holder.imageView)

//        if (item.isVideo == true) {
//            holder.imageView.visibility = View.GONE
//            holder.videoView.visibility = View.VISIBLE
//
//            holder.videoView.setVideoURI(Uri.parse(item.url))
//            holder.videoView.setOnPreparedListener { mediaPlayer ->
//                mediaPlayer.isLooping = true
//                holder.videoView.start()
//            }
//        } else {
//            holder.imageView.visibility = View.VISIBLE
//            holder.videoView.visibility = View.GONE
//
//            Glide.with(holder.imageView.context)
//                .load(item.image)
//                .into(holder.imageView)
//
//            holder.itemView.setOnClickListener { v: View? ->
//                // Handle click, e.g., open link
//                val webpage = Uri.parse(item.url)
//                val intent = Intent(Intent.ACTION_VIEW, webpage)
//                if (intent.resolveActivity(context.getPackageManager()) != null) {
//                    context.startActivity(intent)
//                }
//            }
//        }
    }


    override fun getItemCount(): Int = items.size

    class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val videoView: VideoView = view.findViewById(R.id.videoView)
    }
}

//data class SliderItem(val url: String, val isVideo: Boolean)
