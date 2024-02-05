package com.example.touristagency.mvp.model.tours

import com.example.touristagency.mvp.model.cities.City
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IDataSource {
    @GET("api/Goods")
    fun getTours(): Single<List<Tour>>

    @GET("api/Cities")
    fun getCities(): Single<List<City>>

    @GET("api/Goods/{city}")
    fun getHotelsByCity(@Path("city") city: String): Single<List<Tour>>

}