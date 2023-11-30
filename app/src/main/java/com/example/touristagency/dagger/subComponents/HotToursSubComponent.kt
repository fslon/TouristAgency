package com.example.touristagency.dagger.subComponents

import com.example.touristagency.dagger.scopes.HotToursScope
import com.example.touristagency.mvp.presenter.HotToursPresenter
import dagger.Subcomponent

@HotToursScope
@Subcomponent(
    modules = [
//        UserModule::class
    ]
)

interface HotToursSubComponent {
//    fun repositorySubComponent(): RepositorySubComponent

    fun inject(hotToursPresenter: HotToursPresenter)
//    fun inject(usersRVAdapter: UsersRVAdapter)

}