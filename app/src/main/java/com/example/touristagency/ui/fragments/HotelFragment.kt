package com.example.touristagency.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.touristagency.App
import com.example.touristagency.dagger.subComponents.HotelSubComponent
import com.example.touristagency.databinding.FragmentHotelBinding
import com.example.touristagency.mvp.model.hotels.Hotel
import com.example.touristagency.mvp.presenter.HotelPresenter
import com.example.touristagency.mvp.view.SlideShowAdapter
import com.example.touristagency.mvp.view.HotelView
import com.example.touristagency.ui.activity.BackButtonListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class HotelFragment(tour: Hotel) : MvpAppCompatFragment(), HotelView, BackButtonListener {
    private var _binding: FragmentHotelBinding? = null
    private val binding get() = _binding!!

    private var tourSubComponent: HotelSubComponent? = null


    private lateinit var currentCurrency: String // текущая валюта


    val presenter: HotelPresenter by moxyPresenter {
        tourSubComponent = App.instance.initHotelSubComponent()

        HotelPresenter(tour).apply {
            tourSubComponent?.inject(this)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotelBinding.inflate(inflater, container, false)
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
        binding.recyclerItemHotelHotelName.text = text
    }

    override fun initPlace(text: String) {
        binding.recyclerItemHotelHotelLocation.text = text
    }

    override fun initRating(text: String) {
        binding.recyclerItemHotelHotelRating.text = text
    }

    override fun initAirport(text: String) {
        binding.recyclerItemHotelAirportTextView.text = "в $text км"
    }

    override fun initBeach(text: String) {
        if (text != "0") binding.recyclerItemHotelBeachTextView.text = "$text м"
        else binding.recyclerItemHotelBeachTextView.text = "далеко"
    }

    override fun initParking(text: String) {
        if (text == "+") binding.recyclerItemHotelParkingTextView.text = "есть"
        else binding.recyclerItemHotelParkingTextView.text = "нет"
    }

    override fun initPrice(text: String) {
        binding.recyclerItemHotelPriceTextView.text = "$text ₽ "
    }

    override fun initPictures(pictures: List<String>) {
//        val images = listOf(picture1.toInt(), picture2.toInt(), picture3.toInt())
//        val adapter = SlideShowAdapter(images)
//        binding.recyclerItemTourImageLayoutViewpager2.adapter = adapter
        binding.recyclerItemTourImageLayoutViewpager2.adapter = SlideShowAdapter(pictures)
    }


    override fun release() {
        tourSubComponent = null
        App.instance.releaseHotelSubComponent()
    }

    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance(tour: Hotel) = HotelFragment(tour)
    }


}