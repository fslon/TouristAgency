package com.example.touristagency.dagger.tours

import com.example.touristagency.mvp.model.hotels.Hotel
import io.reactivex.rxjava3.core.Single

interface IHotelsCache {
    fun insertTours(tours: List<Hotel>): Single<List<Hotel>>
}