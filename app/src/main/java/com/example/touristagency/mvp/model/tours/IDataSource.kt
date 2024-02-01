package com.example.touristagency.mvp.model.tours

import com.example.touristagency.mvp.model.cities.City
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface IDataSource {
    @GET("api/Goods")
    fun getTours(): Single<List<Tour>>

    @GET("api/Cities")
    fun getCities(): Single<List<City>>
}