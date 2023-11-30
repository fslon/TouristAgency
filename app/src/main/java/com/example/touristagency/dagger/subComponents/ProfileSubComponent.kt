package com.example.touristagency.dagger.subComponents

import com.example.touristagency.dagger.scopes.ProfileScope
import com.example.touristagency.mvp.presenter.ProfilePresenter
import dagger.Subcomponent


@ProfileScope
@Subcomponent(
    modules = [
//        UserModule::class
    ]
)

interface ProfileSubComponent {

    fun inject(profilePresenter: ProfilePresenter)
//    fun inject(usersRVAdapter: UsersRVAdapter)

}