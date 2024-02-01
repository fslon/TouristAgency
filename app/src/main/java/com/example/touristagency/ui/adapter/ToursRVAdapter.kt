package com.example.touristagency.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.touristagency.R
import com.example.touristagency.databinding.RecyclerItemTourBinding
import com.example.touristagency.mvp.presenter.list.ITourListPresenter
import com.example.touristagency.mvp.view.SlideShowAdapter
import com.example.touristagency.mvp.view.list.TourItemView

class ToursRVAdapter(
    val presenter: ITourListPresenter
) : RecyclerView.Adapter<ToursRVAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        context = parent.context

        return ViewHolder(RecyclerItemTourBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            itemView.setOnClickListener { presenter.itemClickListener?.invoke(this) }
            itemView.findViewById<AppCompatImageView>(R.id.recycler_item_tour_favourite_button).setOnClickListener {
                presenter.favouriteButtonClickListener?.invoke(this)
            }
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
            if (text != "0"){
                vb.recyclerViewBeachLayout.visibility = View.VISIBLE
                vb.recyclerItemTourBeachTextView.text = "$text м"}
            else {
//                vb.recyclerItemTourBeachTextView.text = "далеко"

                vb.recyclerViewBeachLayout.visibility = View.GONE

            }
        }

        override fun setRating(text: String) {
            vb.recyclerItemTourHotelRating.text = text
        }

        override fun setParking(text: String) {
            if (text == "+") vb.recyclerItemTourParkingTextView.text = "есть"
            else vb.recyclerItemTourParkingTextView.text = "нет"
        }

        override fun setStars(text: String) {
            showStars(text)
        }

        override fun setFoodSystem(text: String) {
            //todo
        }

        override fun setFoodType(text: String) {
            //todo
        }

        override fun loadPictures(images: List<String>) {
            vb.recyclerItemTourImageLayoutViewpager2.adapter = SlideShowAdapter(images)
        }

        override fun setWifi(text: String) {
            if (text == "+") {
                vb.recyclerItemTourWifiTextView.text = "везде"
            } else if (text == "-") {
                vb.recyclerItemTourWifiTextView.text = "нет"
            }
        }

        override fun setLine(number: Int) {
            vb.recyclerViewLineLayout.visibility = View.VISIBLE
            when (number) {
                1 -> vb.recyclerItemTourLineImage.setImageResource(R.drawable.first_24)
                2 -> vb.recyclerItemTourLineImage.setImageResource(R.drawable.second_24)
                3 -> vb.recyclerItemTourLineImage.setImageResource(R.drawable.third_24)
                0 -> {
                    vb.recyclerViewLineLayout.visibility = View.GONE

                }
            }


        }


        override var pos = -1


        private fun showStars(numberOfStars: String) {
            when (numberOfStars) {
                "1" -> {
                    vb.recyclerItemTourStar1.isVisible = true
                    vb.recyclerItemTourStar2.isVisible = false
                    vb.recyclerItemTourStar3.isVisible = false
                    vb.recyclerItemTourStar4.isVisible = false
                    vb.recyclerItemTourStar5.isVisible = false
                }

                "2" -> {
                    vb.recyclerItemTourStar1.isVisible = true
                    vb.recyclerItemTourStar2.isVisible = true
                    vb.recyclerItemTourStar3.isVisible = false
                    vb.recyclerItemTourStar4.isVisible = false
                    vb.recyclerItemTourStar5.isVisible = false
                }

                "3" -> {
                    vb.recyclerItemTourStar1.isVisible = true
                    vb.recyclerItemTourStar2.isVisible = true
                    vb.recyclerItemTourStar3.isVisible = true
                    vb.recyclerItemTourStar4.isVisible = false
                    vb.recyclerItemTourStar5.isVisible = false
                }

                "4" -> {
                    vb.recyclerItemTourStar1.isVisible = true
                    vb.recyclerItemTourStar2.isVisible = true
                    vb.recyclerItemTourStar3.isVisible = true
                    vb.recyclerItemTourStar4.isVisible = true
                    vb.recyclerItemTourStar5.isVisible = false
                }

                "5" -> {
                    vb.recyclerItemTourStar1.isVisible = true
                    vb.recyclerItemTourStar2.isVisible = true
                    vb.recyclerItemTourStar3.isVisible = true
                    vb.recyclerItemTourStar4.isVisible = true
                    vb.recyclerItemTourStar5.isVisible = true
                }

                else -> {
                    vb.recyclerItemTourStar1.isVisible = false
                    vb.recyclerItemTourStar2.isVisible = false
                    vb.recyclerItemTourStar3.isVisible = false
                    vb.recyclerItemTourStar4.isVisible = false
                    vb.recyclerItemTourStar5.isVisible = false
                }

            }

        }


    }
}
