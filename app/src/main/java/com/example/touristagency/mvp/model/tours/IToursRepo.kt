package com.example.touristagency.mvp.model.tours

import io.reactivex.rxjava3.core.Single

interface IToursRepo {
    fun getTours(): Single<List<Tour>>
}