package com.example.touristagency.dagger

import com.example.touristagency.dagger.scopes.ToursScope
import com.example.touristagency.mvp.model.cities.ICitiesRepo
import com.example.touristagency.mvp.model.cities.RetrofitCitiesRepo
import com.example.touristagency.mvp.model.tours.IDataSource
import com.example.touristagency.ui.network.INetworkStatus
import dagger.Module
import dagger.Provides

@Module
class CityModule {

    @ToursScope
    @Provides
    fun citiesRepo(
        api: IDataSource, networkStatus: INetworkStatus
    ): ICitiesRepo = RetrofitCitiesRepo(api, networkStatus)

}