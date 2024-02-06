package com.example.touristagency

import android.app.Application
import com.example.touristagency.dagger.AppComponent
import com.example.touristagency.dagger.AppModule
import com.example.touristagency.dagger.DaggerAppComponent
import com.example.touristagency.dagger.subComponents.FavouritesSubComponent
import com.example.touristagency.dagger.subComponents.HotHotelsSubComponent
import com.example.touristagency.dagger.subComponents.ProfileSubComponent
import com.example.touristagency.dagger.subComponents.HotelSubComponent
import com.example.touristagency.dagger.subComponents.HotelsSubComponent


class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    var toursSubComponent: HotelsSubComponent? = null
        private set

    var hotHotelsSubComponent: HotHotelsSubComponent? = null
        private set

    var favouritesSubComponent: FavouritesSubComponent? = null
        private set

    var profileSubComponent: ProfileSubComponent? = null
        private set

    var hotelSubComponent: HotelSubComponent? = null
        private set

    companion object {
        lateinit var instance: App
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this))
            .build()
    }

    fun initUserSubComponent() = appComponent.hotelsSubComponent().also {
        toursSubComponent = it
    }

    fun releaseUserSubComponent() {
        toursSubComponent = null
    }

    fun initHotHotelsSubComponent() = appComponent.hotHotelsSubComponent().also {
        hotHotelsSubComponent = it
    }

    fun releaseHotHotelsSubComponent() {
        hotHotelsSubComponent = null
    }

    fun initFavouritesSubComponent() = appComponent.favouritesSubComponent().also {
        favouritesSubComponent = it
    }

    fun releaseFavouritesSubComponent() {
        favouritesSubComponent = null
    }

    fun initProfileSubComponent() = appComponent.profileSubComponent().also {
        profileSubComponent = it
    }

    fun releaseProfileSubComponent() {
        profileSubComponent = null
    }

    fun initHotelSubComponent() = appComponent.hotelSubComponent().also {
        hotelSubComponent = it
    }

    fun releaseHotelSubComponent() {
        hotelSubComponent = null
    }

}


