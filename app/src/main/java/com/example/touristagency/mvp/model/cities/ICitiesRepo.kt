package com.example.touristagency.mvp.model.cities

import io.reactivex.rxjava3.core.Single

interface ICitiesRepo {
    fun getCities(): Single<List<City>>
}