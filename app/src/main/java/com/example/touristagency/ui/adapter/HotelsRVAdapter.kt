package com.example.touristagency.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.touristagency.R
import com.example.touristagency.databinding.RecyclerItemHotelBinding
import com.example.touristagency.mvp.presenter.list.IHotelListPresenter
import com.example.touristagency.mvp.view.SlideShowAdapter
import com.example.touristagency.mvp.view.list.HotelItemView

class HotelsRVAdapter(
    val presenter: IHotelListPresenter
) : RecyclerView.Adapter<HotelsRVAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        context = parent.context

        return ViewHolder(RecyclerItemHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            itemView.setOnClickListener { presenter.itemClickListener?.invoke(this) }
            itemView.findViewById<AppCompatImageView>(R.id.recycler_item_hotel_favourite_button).setOnClickListener {
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


    inner class ViewHolder(val vb: RecyclerItemHotelBinding) : RecyclerView.ViewHolder(vb.root), HotelItemView {

        val favouriteImageView: AppCompatImageView = itemView.findViewById(R.id.recycler_item_hotel_favourite_button)

        override fun setName(text: String) {
            vb.recyclerItemHotelHotelName.text = text
        }

        override fun setPlace(text: String) {
            vb.recyclerItemHotelHotelLocation.text = text
        }

        override fun setPrice(text: String) {
            vb.recyclerItemHotelPriceTextView.text = "$text ₽ "
        }

        override fun setAirportDistance(text: String) {
            vb.recyclerItemHotelAirportTextView.text = "в $text км"
        }

        override fun setBeachDistance(text: String) {
            if (text != "0") {
                vb.recyclerViewBeachLayout.visibility = View.VISIBLE
                vb.recyclerItemHotelBeachTextView.text = "$text м"
            } else {
//                vb.recyclerItemTourBeachTextView.text = "далеко"

                vb.recyclerViewBeachLayout.visibility = View.GONE

            }
        }

        override fun setRating(text: String) {
            vb.recyclerItemHotelHotelRating.text = text
        }

        override fun setParking(text: String) {
            if (text == "+") vb.recyclerItemHotelParkingTextView.text = "есть"
            else vb.recyclerItemHotelParkingTextView.text = "нет"
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
                vb.recyclerItemHotelWifiTextView.text = "везде"
            } else if (text == "-") {
                vb.recyclerItemHotelWifiTextView.text = "нет"
            }
        }

        override fun setLine(number: Int) {
            vb.recyclerViewLineLayout.visibility = View.VISIBLE
            when (number) {
                1 -> vb.recyclerItemHotelLineImage.setImageResource(R.drawable.first_24)
                2 -> vb.recyclerItemHotelLineImage.setImageResource(R.drawable.second_24)
                3 -> vb.recyclerItemHotelLineImage.setImageResource(R.drawable.third_24)
                0 -> {
                    vb.recyclerViewLineLayout.visibility = View.GONE

                }
            }


        }


        override var pos = -1


        private fun showStars(numberOfStars: String) {
            when (numberOfStars) {
                "1" -> {
                    vb.recyclerItemHotelStar1.isVisible = true
                    vb.recyclerItemHotelStar2.isVisible = false
                    vb.recyclerItemHotelStar3.isVisible = false
                    vb.recyclerItemHotelStar4.isVisible = false
                    vb.recyclerItemHotelStar5.isVisible = false
                }

                "2" -> {
                    vb.recyclerItemHotelStar1.isVisible = true
                    vb.recyclerItemHotelStar2.isVisible = true
                    vb.recyclerItemHotelStar3.isVisible = false
                    vb.recyclerItemHotelStar4.isVisible = false
                    vb.recyclerItemHotelStar5.isVisible = false
                }

                "3" -> {
                    vb.recyclerItemHotelStar1.isVisible = true
                    vb.recyclerItemHotelStar2.isVisible = true
                    vb.recyclerItemHotelStar3.isVisible = true
                    vb.recyclerItemHotelStar4.isVisible = false
                    vb.recyclerItemHotelStar5.isVisible = false
                }

                "4" -> {
                    vb.recyclerItemHotelStar1.isVisible = true
                    vb.recyclerItemHotelStar2.isVisible = true
                    vb.recyclerItemHotelStar3.isVisible = true
                    vb.recyclerItemHotelStar4.isVisible = true
                    vb.recyclerItemHotelStar5.isVisible = false
                }

                "5" -> {
                    vb.recyclerItemHotelStar1.isVisible = true
                    vb.recyclerItemHotelStar2.isVisible = true
                    vb.recyclerItemHotelStar3.isVisible = true
                    vb.recyclerItemHotelStar4.isVisible = true
                    vb.recyclerItemHotelStar5.isVisible = true
                }

                else -> {
                    vb.recyclerItemHotelStar1.isVisible = false
                    vb.recyclerItemHotelStar2.isVisible = false
                    vb.recyclerItemHotelStar3.isVisible = false
                    vb.recyclerItemHotelStar4.isVisible = false
                    vb.recyclerItemHotelStar5.isVisible = false
                }

            }

        }


    }
}
