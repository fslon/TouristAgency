package com.example.touristagency.mvp.model.hotels

import io.reactivex.rxjava3.core.Single

interface IHotelsRepo {
    fun getHotels(): Single<List<Hotel>>

    fun getHotelsByCity(city: String): Single<List<Hotel>>
}