package com.example.touristagency.dagger.subComponents

import com.example.touristagency.dagger.scopes.ToursScope
import com.example.touristagency.dagger.user.UserModule
import com.example.touristagency.mvp.presenter.ToursMainPresenter
import com.example.touristagency.ui.adapter.ToursRVAdapter
import dagger.Subcomponent

@ToursScope
@Subcomponent(
    modules = [
        UserModule::class
    ]
)

interface ToursSubComponent {


    fun inject(toursMainPresenter: ToursMainPresenter)
    fun inject(toursRVAdapter: ToursRVAdapter)

}