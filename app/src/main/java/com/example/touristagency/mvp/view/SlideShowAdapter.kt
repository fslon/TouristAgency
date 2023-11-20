package com.example.touristagency.mvp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.touristagency.R

class SlideShowAdapter(private val images: List<Int>) : RecyclerView.Adapter<SlideShowAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slide_item, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
//        holder.bind(images[position])
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(imageResId: Int) {
            itemView.findViewById<AppCompatImageView>(R.id.slide_item_image_view).setImageResource(imageResId)
        }
    }
}