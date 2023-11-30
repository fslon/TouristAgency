package com.example.touristagency.mvp.model.users

import retrofit2.Call

interface IUsersRepo {
    fun login(login: String, password: String): Call<Any>
    fun register(login: String, password: String, email: String, birthdate: String, name: String, lastname: String, surname: String): Call<Any>
}