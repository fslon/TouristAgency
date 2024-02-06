package com.example.touristagency.mvp.model.cities

import com.example.touristagency.mvp.model.hotels.IDataSource
import com.example.touristagency.ui.network.INetworkStatus
import io.reactivex.rxjava3.schedulers.Schedulers

class RetrofitCitiesRepo(val api: IDataSource, val networkStatus: INetworkStatus) : ICitiesRepo {

    override fun getCities() = networkStatus.isOnlineSingle().flatMap { isOnline ->
        api.getCities()
    }.subscribeOn(Schedulers.io())


}
