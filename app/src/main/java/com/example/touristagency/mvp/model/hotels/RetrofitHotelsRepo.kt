package com.example.touristagency.mvp.model.hotels

import com.example.touristagency.dagger.tours.IHotelsCache
import com.example.touristagency.ui.network.INetworkStatus
import io.reactivex.rxjava3.schedulers.Schedulers

class RetrofitHotelsRepo(
    val api: IDataSource, val networkStatus: INetworkStatus, val cacheInterface: IHotelsCache
) : IHotelsRepo {
    override fun getHotels() = networkStatus.isOnlineSingle().flatMap { isOnline
        ->
        api.getTours()
            .flatMap { tours ->
                cacheInterface.insertTours(tours)
            }
    }.subscribeOn(Schedulers.io())

    override fun getHotelsByCity(city: String) = networkStatus.isOnlineSingle().flatMap { isOnline
        ->
        api.getHotelsByCity(city).flatMap { hotels ->
//            Log.e("========== ",  city)
            cacheInterface.insertTours(hotels)
        }
    }.subscribeOn(Schedulers.io())

} // todo переименовать файлы на hotels