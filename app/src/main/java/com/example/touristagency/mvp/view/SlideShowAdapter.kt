package com.example.touristagency.mvp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.mvp.view.glide.IImageLoader
import javax.inject.Inject

class SlideShowAdapter(private val images: List<String>) : RecyclerView.Adapter<SlideShowAdapter.SlideViewHolder>() {

    @Inject
    lateinit var imageLoader: IImageLoader<ImageView>

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        App.instance.toursSubComponent?.inject(this)
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slide_item, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.setImage(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }


    inner class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setImage(imageUrl: String) {
            imageLoader.loadInto(imageUrl, itemView.findViewById<AppCompatImageView>(R.id.slide_item_image_view))
        }


    }
}