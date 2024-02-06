package com.example.touristagency.dagger

import com.example.touristagency.dagger.scopes.HotelsScope
import com.example.touristagency.dagger.tours.IHotelsCache
import com.example.touristagency.dagger.tours.RoomHotelsCache
import com.example.touristagency.mvp.model.hotels.IDataSource
import com.example.touristagency.mvp.model.hotels.IHotelsRepo
import com.example.touristagency.mvp.model.hotels.RetrofitHotelsRepo
import com.example.touristagency.ui.network.INetworkStatus
import dagger.Module
import dagger.Provides

@Module
class HotelModule {

    @Provides
    fun toursCache(): IHotelsCache =
        RoomHotelsCache()

    @HotelsScope
    @Provides
    fun toursRepo(
        api: IDataSource, networkStatus: INetworkStatus, cache:
        IHotelsCache
    ): IHotelsRepo = RetrofitHotelsRepo(api, networkStatus, cache)

}