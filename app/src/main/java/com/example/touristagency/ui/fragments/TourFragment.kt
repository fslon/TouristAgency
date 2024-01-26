package com.example.touristagency.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.touristagency.App
import com.example.touristagency.dagger.subComponents.TourSubComponent
import com.example.touristagency.databinding.FragmentTourBinding
import com.example.touristagency.mvp.model.tours.Tour
import com.example.touristagency.mvp.presenter.TourPresenter
import com.example.touristagency.mvp.view.SlideShowAdapter
import com.example.touristagency.mvp.view.TourView
import com.example.touristagency.ui.activity.BackButtonListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class TourFragment(tour: Tour) : MvpAppCompatFragment(), TourView, BackButtonListener {
    private var _binding: FragmentTourBinding? = null
    private val binding get() = _binding!!

    private var tourSubComponent: TourSubComponent? = null


    private lateinit var currentCurrency: String // текущая валюта


    val presenter: TourPresenter by moxyPresenter {
        tourSubComponent = App.instance.initTourSubComponent()

        TourPresenter(tour).apply {
            tourSubComponent?.inject(this)
        }
    }


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

    override fun startCall(intent: Intent) {
        // запускаем намерение
        startActivity(intent)
    }

    override fun initBuyButton() {
        binding.buyTourButton.setOnClickListener {
            presenter.buyTourButtonOnClick()
        }
    }

    override fun initName(text: String) {
        binding.recyclerItemTourHotelName.text = text
    }

    override fun initPlace(text: String) {
        binding.recyclerItemTourHotelLocation.text = text
    }

    override fun initRating(text: String) {
        binding.recyclerItemTourHotelRating.text = text
    }

    override fun initAirport(text: String) {
        binding.recyclerItemTourAirportTextView.text = "в $text км"
    }

    override fun initBeach(text: String) {
        if (text != "0") binding.recyclerItemTourBeachTextView.text = "$text м"
        else binding.recyclerItemTourBeachTextView.text = "далеко"
    }

    override fun initParking(text: String) {
        if (text == "+") binding.recyclerItemTourParkingTextView.text = "есть"
        else binding.recyclerItemTourParkingTextView.text = "нет"
    }

    override fun initPrice(text: String) {
        binding.recyclerItemTourPriceTextView.text = "$text ₽ "
    }

    override fun initPictures(pictures: List<String>) {
//        val images = listOf(picture1.toInt(), picture2.toInt(), picture3.toInt())
//        val adapter = SlideShowAdapter(images)
//        binding.recyclerItemTourImageLayoutViewpager2.adapter = adapter
        binding.recyclerItemTourImageLayoutViewpager2.adapter = SlideShowAdapter(pictures)
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
        fun newInstance(tour: Tour) = TourFragment(tour)
    }


}