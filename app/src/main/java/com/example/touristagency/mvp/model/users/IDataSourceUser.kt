package com.example.touristagency.mvp.model.users

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IDataSourceUser {
//    @POST("/api/Users/register")
//    fun registerUser(): Single<List<User>>

//    @POST("/api/Users/login")
//    fun loginUser(): Single<List<User>>

    @FormUrlEncoded
    @POST("/api/Users/login")
    fun loginUser(
        @Field("login") login: String,
        @Field("password") password: String
    ): Call<Any>

    @FormUrlEncoded
    @POST("/api/Users/register")
    fun registerUser(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("birthdate") birthdate: String,
        @Field("name") name: String,
        @Field("lastname") lastname: String,
        @Field("surname") surname: String,
    ): Call<Any>
}