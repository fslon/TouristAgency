package com.example.touristagency.dagger

import com.example.touristagency.dagger.scopes.HotelsScope
import com.example.touristagency.mvp.model.cities.ICitiesRepo
import com.example.touristagency.mvp.model.cities.RetrofitCitiesRepo
import com.example.touristagency.mvp.model.hotels.IDataSource
import com.example.touristagency.ui.network.INetworkStatus
import dagger.Module
import dagger.Provides

@Module
class CityModule {

    @HotelsScope
    @Provides
    fun citiesRepo(
        api: IDataSource, networkStatus: INetworkStatus
    ): ICitiesRepo = RetrofitCitiesRepo(api, networkStatus)

}