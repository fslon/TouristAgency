package com.example.touristagency.mvp.model.users

import io.reactivex.rxjava3.core.Single
import retrofit2.http.POST

interface IDataSourceUser {
    @POST("/api/Users/register")
    fun registerUser(): Single<List<User>>

    @POST("/api/Users/login")
    fun loginUser(): Single<List<User>>
}