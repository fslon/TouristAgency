package com.example.touristagency.dagger

import com.example.touristagency.dagger.user.UserModule
import com.example.touristagency.mvp.presenter.ToursMainPresenter
import dagger.Subcomponent

@ToursScope
@Subcomponent(
    modules = [
        UserModule::class
    ]
)

interface ToursSubComponent {
    fun hotToursSubComponent(): HotToursSubComponent

    fun inject(toursMainPresenter: ToursMainPresenter)
//    fun inject(usersRVAdapter: UsersRVAdapter)

}