package com.example.touristagency.dagger.subComponents

import com.example.touristagency.dagger.CityModule
import com.example.touristagency.dagger.HotelModule
import com.example.touristagency.dagger.scopes.HotelsScope
import com.example.touristagency.mvp.presenter.HotelsMainPresenter
import com.example.touristagency.mvp.view.SlideShowAdapter
import dagger.Subcomponent

@HotelsScope
@Subcomponent(
    modules = [
        HotelModule::class,
        CityModule::class
    ]
)

interface HotelsSubComponent {

    fun inject(hotelsMainPresenter: HotelsMainPresenter)

    fun inject(slideShowAdapter: SlideShowAdapter)

}