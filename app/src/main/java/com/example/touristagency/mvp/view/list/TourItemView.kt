package com.example.touristagency.mvp.view.list

interface TourItemView : IItemView {
    fun setName(text: String)
    fun setPlace(text: String)
    fun setPrice(text: String)
    fun setAirportDistance(text: String)
    fun setBeachDistance(text: String)
    fun setRating(text: String)
    fun setParking(text: String)
    fun setStars(text: String)
    fun setFoodSystem(text: String)
    fun setFoodType(text: String)

    fun loadPicture1(url: String)
    fun loadPicture2(url: String)
    fun loadPicture3(url: String)

}