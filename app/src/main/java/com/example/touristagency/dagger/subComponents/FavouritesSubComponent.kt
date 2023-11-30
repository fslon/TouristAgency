package com.example.touristagency.dagger.subComponents

import com.example.touristagency.dagger.scopes.FavouritesScope
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