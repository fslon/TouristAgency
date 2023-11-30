package com.example.touristagency.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.subComponents.TourSubComponent
import com.example.touristagency.databinding.FragmentTourBinding
import com.example.touristagency.mvp.presenter.TourPresenter
import com.example.touristagency.mvp.view.SlideShowAdapter
import com.example.touristagency.mvp.view.TourView
import com.example.touristagency.ui.activity.BackButtonListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class TourFragment : MvpAppCompatFragment(), TourView, BackButtonListener {
    private var _binding: FragmentTourBinding? = null
    private val binding get() = _binding!!

    private var tourSubComponent: TourSubComponent? = null


    private lateinit var currentCurrency: String // текущая валюта


    val presenter: TourPresenter by moxyPresenter {
        tourSubComponent = App.instance.initTourSubComponent()

        TourPresenter().apply {
            tourSubComponent?.inject(this)
        }
    }

//    var adapter: UsersRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initCurrentCurrency(currentCurrency: String) {
        this.currentCurrency = currentCurrency
    }


    override fun testInitFirstRecyclerItem() { // прокручивающиеся картинки в recyclerViewItem // todo переработать

        binding.recyclerItemTourHotelName.text = "«Бургас» пансионат"
        binding.recyclerItemTourHotelLocation.text = "г. Сочи, п. Кудепста"
        binding.recyclerItemTourHotelRating.text = "9.1"

        binding.recyclerItemTourAirportTextView.text = "в 6 км"
        binding.recyclerItemTourBeachTextView.text = "150 м"

        binding.recyclerItemTourPriceTextView.text = "96 583 $currentCurrency"


        binding.recyclerItemTourFavouriteButton.setOnClickListener {
            // todo прокинуть метод в presenter
        }
        // Создаем список изображений
        val images = listOf(
            R.drawable.burgas_1,
            R.drawable.burgas_2,
            R.drawable.burgas_3
        )

        binding.recyclerItemTourLineImage.setImageResource(R.drawable.first_24)

// Создаем экземпляр PagerAdapter и устанавливаем его во ViewPager2
        val adapter = SlideShowAdapter(images)
        binding.recyclerItemTourImageLayoutViewpager2.adapter = adapter

        binding.recyclerItemTourFavouriteButton.setOnClickListener {
            binding.recyclerItemTourFavouriteButton.setImageResource(R.drawable.baseline_favorite_24)
        }

        binding.buyTourButton.setOnClickListener {
            presenter.buyTourButtonOnClick()
        }
    }

    override fun startCall(intent: Intent) {
        // запускаем намерение
        startActivity(intent)
    }


    override fun release() {
        tourSubComponent = null
        App.instance.releaseTourSubComponent()
    }

    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() = TourFragment()
    }


}