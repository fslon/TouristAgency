package com.example.touristagency.dagger

import com.example.touristagency.dagger.scopes.ToursScope
import com.example.touristagency.dagger.tours.IToursCache
import com.example.touristagency.dagger.tours.RoomToursCache
import com.example.touristagency.mvp.model.tours.IDataSource
import com.example.touristagency.mvp.model.tours.IToursRepo
import com.example.touristagency.mvp.model.tours.RetrofitToursRepo
import com.example.touristagency.ui.network.INetworkStatus
import dagger.Module
import dagger.Provides

@Module
class TourModule {

    @Provides
    fun toursCache(): IToursCache =
        RoomToursCache()

    @ToursScope
    @Provides
    fun toursRepo(
        api: IDataSource, networkStatus: INetworkStatus, cache:
        IToursCache
    ): IToursRepo = RetrofitToursRepo(api, networkStatus, cache)

}