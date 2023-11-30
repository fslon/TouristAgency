package com.example.touristagency.dagger.subComponents

import com.example.touristagency.dagger.scopes.TourScope
import com.example.touristagency.mvp.presenter.TourPresenter
import dagger.Subcomponent


@TourScope
@Subcomponent(
    modules = [
//        UserModule::class
    ]
)

interface TourSubComponent {

    fun inject(tourPresenter: TourPresenter)
//    fun inject(usersRVAdapter: UsersRVAdapter)

}