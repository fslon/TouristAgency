package com.example.touristagency.dagger

import com.example.touristagency.mvp.presenter.FavouritesPresenter
import dagger.Subcomponent


@FavouritesScope
@Subcomponent(
    modules = [
//        UserModule::class
    ]
)

interface FavouritesSubComponent {

    fun inject(favouritesPresenter: FavouritesPresenter)
//    fun inject(usersRVAdapter: UsersRVAdapter)

}