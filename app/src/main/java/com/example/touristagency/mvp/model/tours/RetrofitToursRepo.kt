package com.example.touristagency.mvp.model.tours

import android.util.Log
import com.example.touristagency.dagger.tours.IToursCache
import com.example.touristagency.ui.network.INetworkStatus
import io.reactivex.rxjava3.schedulers.Schedulers

class RetrofitToursRepo(
    val api: IDataSource, val networkStatus: INetworkStatus, val cacheInterface: IToursCache
//    val api: IDataSource, val networkStatus: INetworkStatus
) : IToursRepo {
    override fun getTours() = networkStatus.isOnlineSingle().flatMap { isOnline
        ->
        api.getTours()
            .flatMap { tours ->
                cacheInterface.insertTours(tours)
            }
    }.subscribeOn(Schedulers.io())

    override fun getHotelsByCity(city: String) = networkStatus.isOnlineSingle().flatMap { isOnline
        ->
        api.getHotelsByCity(city).flatMap { hotels ->
            Log.e("========== ",  city)
            cacheInterface.insertTours(hotels)
        }
    }.subscribeOn(Schedulers.io())

}