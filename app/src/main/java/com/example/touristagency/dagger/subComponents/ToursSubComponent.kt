package com.example.touristagency.dagger.subComponents

import com.example.touristagency.dagger.TourModule
import com.example.touristagency.dagger.scopes.ToursScope
import com.example.touristagency.mvp.presenter.ToursMainPresenter
import com.example.touristagency.ui.adapter.ToursRVAdapter
import dagger.Subcomponent

@ToursScope
@Subcomponent(
    modules = [
        TourModule::class
    ]
)

interface ToursSubComponent {

    fun inject(toursMainPresenter: ToursMainPresenter)
    fun inject(toursRVAdapter: ToursRVAdapter)

}