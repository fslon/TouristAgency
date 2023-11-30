package com.example.touristagency.mvp.model.tours

import com.example.touristagency.mvp.model.users.User
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface IDataSource {
//    @GET("/api/Users")
//    fun getUsers(): Single<List<User>>

    @GET("/api/Tours")
    fun getTours(): Single<List<Tour>>
}