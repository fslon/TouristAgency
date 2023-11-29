package com.example.touristagency.mvp.model.users

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface IDataSource {
    @GET("/api/Users")
    fun getUsers(): Single<List<User>>

}