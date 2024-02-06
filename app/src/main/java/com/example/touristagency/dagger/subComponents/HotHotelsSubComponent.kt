package com.example.touristagency.dagger.subComponents

import com.example.touristagency.dagger.scopes.HotHotelsScope
import com.example.touristagency.mvp.presenter.HotHotelsPresenter
import com.example.touristagency.ui.adapter.HotelsRVAdapter
import dagger.Subcomponent

@HotHotelsScope
@Subcomponent(
    modules = [
//        UserModule::class
    ]
)

interface HotHotelsSubComponent {
//    fun repositorySubComponent(): RepositorySubComponent

    fun inject(hotHotelsPresenter: HotHotelsPresenter)
    fun inject(hotelsRVAdapter: HotelsRVAdapter)

}