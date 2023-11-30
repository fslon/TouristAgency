package com.example.touristagency.mvp.model.users

import com.example.touristagency.mvp.model.Tour
import io.reactivex.rxjava3.core.Single

interface IToursAndUsersRepo {
    fun getUsers(): Single<List<User>>
    fun getTours(): Single<List<Tour>>
}