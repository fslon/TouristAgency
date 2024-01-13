package com.example.touristagency.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.touristagency.R
import com.example.touristagency.databinding.RecyclerItemTourBinding
import com.example.touristagency.mvp.presenter.list.ITourListPresenter
import com.example.touristagency.mvp.view.glide.IImageLoader
import com.example.touristagency.mvp.view.list.TourItemView
import javax.inject.Inject

class ToursRVAdapter(
    val presenter: ITourListPresenter
) : RecyclerView.Adapter<ToursRVAdapter.ViewHolder>() {

    @Inject
    lateinit var imageLoader: IImageLoader<ImageView>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(RecyclerItemTourBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            itemView.setOnClickListener { presenter.itemClickListener?.invoke(this) }
            itemView.findViewById<AppCompatImageView>(R.id.recycler_item_tour_favourite_button).setOnClickListener {
                presenter.favouriteButtonClickListener?.invoke(this)
            }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        presenter.bindView(holder.apply { pos = position })
//        holder.favouriteImageView.setOnClickListener {
//            presenter.updateImage(position)
//        }

    }

    override fun getItemCount() = presenter.getCount()


    inner class ViewHolder(val vb: RecyclerItemTourBinding) : RecyclerView.ViewHolder(vb.root), TourItemView {
        val favouriteImageView: AppCompatImageView = itemView.findViewById(R.id.recycler_item_tour_favourite_button)
        override fun setName(text: String) {
            vb.recyclerItemTourHotelName.text = text
        }

        override fun setPlace(text: String) {
            vb.recyclerItemTourHotelLocation.text = text
        }

        override fun setPrice(text: String) {
            vb.recyclerItemTourPriceTextView.text = "$text ₽ "
        }

        override fun setAirportDistance(text: String) {
            vb.recyclerItemTourAirportTextView.text = "в $text км"
        }

        override fun setBeachDistance(text: String) {
            if (text != "0") vb.recyclerItemTourBeachTextView.text = "$text м"
            else vb.recyclerItemTourBeachTextView.text = "далеко"
        }

        override fun setRating(text: String) {
            vb.recyclerItemTourHotelRating.text = text
        }

        override fun setParking(text: String) {
            if (text == "+") vb.recyclerItemTourParkingTextView.text = "есть"
            else vb.recyclerItemTourParkingTextView.text = "нет"
        }

        override fun setStars(text: String) {
            //todo
        }

        override fun setFoodSystem(text: String) {
            //todo
        }

        override fun setFoodType(text: String) {
            //todo
        }

        override fun loadPicture1(url: String) {
//            imageLoader.loadInto(url, vb.recyclerItemTourImageLayoutViewpager2.)
        }

        override fun loadPicture2(url: String) {
//            imageLoader.loadInto(url, vb.recyclerItemTourImageLayoutViewpager2)
        }

        override fun loadPicture3(url: String) {
//            imageLoader.loadInto(url, vb.recyclerItemTourImageLayoutViewpager2)
        }

        override var pos = -1


    }
}
