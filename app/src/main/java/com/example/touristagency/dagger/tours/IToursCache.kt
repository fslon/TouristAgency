package com.example.touristagency.dagger.tours

import com.example.touristagency.mvp.model.tours.Tour
import io.reactivex.rxjava3.core.Single

interface IToursCache {
    fun insertTours(tours: List<Tour>): Single<List<Tour>>
}