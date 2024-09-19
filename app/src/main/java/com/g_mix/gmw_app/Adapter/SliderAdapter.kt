package com.g_mix.gmw_app.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.R

class SliderAdapter(private val items: List<SliderItem>) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = items[position]

        // Reset the VideoView and stop playback before setting a new URI
        holder.videoView.stopPlayback()
        holder.videoView.setVideoURI(null)

        if (item.isVideo) {
            holder.imageView.visibility = View.GONE
            holder.videoView.visibility = View.VISIBLE

            // Set video URI and prepare it
            holder.videoView.setVideoURI(Uri.parse(item.url))
            holder.videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true  // Optional
                holder.videoView.start()
            }
        } else {
            holder.imageView.visibility = View.VISIBLE
            holder.videoView.visibility = View.GONE

            // Load the image with Glide
            Glide.with(holder.imageView.context)
                .load(item.url)
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = items.size

    class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val videoView: VideoView = view.findViewById(R.id.videoView)
    }
}

data class SliderItem(val url: String, val isVideo: Boolean)
