package com.example.touristagency.mvp.view

import android.content.Intent
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TourView : MvpView {
    //    fun init()
//    fun updateList()
    fun release()
//    fun testInitFirstRecyclerItem()

    fun initCurrentCurrency(currentCurrency: String)

    fun startCall(intent: Intent)
    fun initBuyButton()

    fun initName(text: String)
    fun initPlace(text: String)
    fun initRating(text: String)
    fun initAirport(text: String)
    fun initBeach(text: String)
    fun initParking(text: String)
    fun initPrice(text: String)
    fun initPictures(picture1: String, picture2: String, picture3: String)


}