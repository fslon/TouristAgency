package com.example.touristagency.dagger.subComponents

import com.example.touristagency.dagger.scopes.HotelScope
import com.example.touristagency.mvp.presenter.HotelPresenter
import dagger.Subcomponent


@HotelScope
@Subcomponent(
    modules = [
//        UserModule::class
    ]
)

interface HotelSubComponent {

    fun inject(hotelPresenter: HotelPresenter)
//    fun inject(usersRVAdapter: UsersRVAdapter)

}