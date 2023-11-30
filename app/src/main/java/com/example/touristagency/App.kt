package com.example.touristagency

import android.app.Application
import com.example.touristagency.dagger.AppComponent
import com.example.touristagency.dagger.AppModule
import com.example.touristagency.dagger.DaggerAppComponent
import com.example.touristagency.dagger.subComponents.FavouritesSubComponent
import com.example.touristagency.dagger.subComponents.HotToursSubComponent
import com.example.touristagency.dagger.subComponents.ProfileSubComponent
import com.example.touristagency.dagger.subComponents.TourSubComponent
import com.example.touristagency.dagger.subComponents.ToursSubComponent


class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    var toursSubComponent: ToursSubComponent? = null
        private set

    var hotToursSubComponent: HotToursSubComponent? = null
        private set

    var favouritesSubComponent: FavouritesSubComponent? = null
        private set

    var profileSubComponent: ProfileSubComponent? = null
        private set

    var tourSubComponent: TourSubComponent? = null
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

    fun initUserSubComponent() = appComponent.toursSubcomponent().also {
        toursSubComponent = it
    }

    fun releaseUserSubComponent() {
        toursSubComponent = null
    }

    fun initHotToursSubComponent() = appComponent.hotToursSubComponent().also {
        hotToursSubComponent = it
    }

    fun releaseHotToursSubComponent() {
        hotToursSubComponent = null
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

    fun initTourSubComponent() = appComponent.tourSubComponent().also {
        tourSubComponent = it
    }

    fun releaseTourSubComponent() {
        tourSubComponent = null
    }

}


