package com.example.touristagency

import android.app.Application
import com.example.touristagency.dagger.AppComponent
import com.example.touristagency.dagger.AppModule
import com.example.touristagency.dagger.DaggerAppComponent
import com.example.touristagency.dagger.ToursSubComponent


class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    var toursSubComponent: ToursSubComponent? = null
        private set

//    var repositorySubComponent: RepositorySubComponent? = null
//        private set

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

//    fun initRepositorySubComponent() = userSubComponent?.repositorySubComponent().also {
//        repositorySubComponent = it
//    }
//
//    fun releaseRepositorySubComponent() {
//        repositorySubComponent = null
    }


