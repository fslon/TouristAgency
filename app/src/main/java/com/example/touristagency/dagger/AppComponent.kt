package com.example.touristagency.dagger

import com.example.touristagency.ui.activity.MainActivity
import com.example.touristagency.mvp.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        AppModule::class,
        CacheModule::class,
        CiceroneModule::class,
//        ImageModule::class
////        RepoModule::class
    ]
)
interface AppComponent {
    fun toursSubcomponent(): ToursSubComponent

    fun inject(mainActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)

//    fun inject(usersPresenter: UsersPresenter)
//    fun inject(repositoryPresenter: RepositoryPresenter)
//    fun inject(profilePresenter: ProfilePresenter)

}