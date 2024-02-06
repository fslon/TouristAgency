package com.example.touristagency.mvp.model.hotels

import com.example.touristagency.mvp.model.cities.City
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface IDataSource {
    @GET("api/Goods")
    fun getTours(): Single<List<Hotel>>

    @GET("api/Cities")
    fun getCities(): Single<List<City>>

    @GET("api/Goods/{city}")
    fun getHotelsByCity(@Path("city") city: String): Single<List<Hotel>>

}