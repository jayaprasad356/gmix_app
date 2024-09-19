package com.g_mix.gmw_app.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.g_mix.gmw_app.Model.MediaItem
import com.g_mix.gmw_app.R

class MediaSliderAdapter(private val mediaList: List<MediaItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_IMAGE = 0
        const val TYPE_VIDEO = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_IMAGE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            ImageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
            VideoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mediaItem = mediaList[position]
        if (holder is ImageViewHolder) {
            // Load image using Glide or any image loading library
            Glide.with(holder.itemView.context)
                .load(mediaItem.url)
                .into(holder.imageView)
        } else if (holder is VideoViewHolder) {
            // Set up video view
            holder.videoView.setVideoURI(Uri.parse(mediaItem.url))
            holder.videoView.start()
        }
    }

    override fun getItemCount(): Int = mediaList.size

    override fun getItemViewType(position: Int): Int {
        return if (mediaList[position].type == "image") TYPE_IMAGE else TYPE_VIDEO
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoView: VideoView = view.findViewById(R.id.videoView)
    }
}
