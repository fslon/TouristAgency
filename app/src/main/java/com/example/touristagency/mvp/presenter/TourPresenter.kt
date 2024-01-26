package com.example.touristagency.mvp.presenter

import android.content.Intent
import android.net.Uri
import com.example.touristagency.mvp.model.tours.Tour
import com.example.touristagency.mvp.view.TourView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import javax.inject.Inject

class TourPresenter(val tour: Tour) : MvpPresenter<TourView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    private val currentCurrency: String = "₽"// текущая валюта


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.initCurrentCurrency(currentCurrency)

        viewState.initBuyButton()

        initViews()

    }

    private fun initViews() {
        viewState.initName(tour.name.toString())
        viewState.initPlace(tour.place.toString())
        viewState.initRating(tour.rating.toString())
        viewState.initAirport(tour.airportDistance.toString())
        viewState.initBeach(tour.beachDistance.toString())
        viewState.initParking(tour.parking.toString())
        viewState.initPrice(tour.price.toString())

        val pictures = mutableListOf<String>()
        pictures.add(tour.photo1.toString())
        pictures.add(tour.photo2.toString())
        pictures.add(tour.photo3.toString())
        viewState.initPictures(pictures)
    }

    fun buyTourButtonOnClick() {
        // создаем намерение для открытия приложения звонки
        val intent = Intent(Intent.ACTION_DIAL)

        // набираем определенный номер
        intent.data = Uri.parse("tel:100")

        viewState.startCall(intent)
    }


    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewState.release()
    }
}