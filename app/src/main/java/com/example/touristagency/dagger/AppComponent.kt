package com.example.touristagency.dagger

import com.example.touristagency.dagger.subComponents.FavouritesSubComponent
import com.example.touristagency.dagger.subComponents.HotHotelsSubComponent
import com.example.touristagency.dagger.subComponents.ProfileSubComponent
import com.example.touristagency.dagger.subComponents.HotelSubComponent
import com.example.touristagency.dagger.subComponents.HotelsSubComponent
import com.example.touristagency.mvp.presenter.MainPresenter
import com.example.touristagency.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        AppModule::class,
        CacheModule::class,
        CiceroneModule::class,
//        TourModule::class
        ImageModule::class
    ]
)
interface AppComponent {
    fun hotelsSubComponent(): HotelsSubComponent
    fun hotHotelsSubComponent(): HotHotelsSubComponent
    fun favouritesSubComponent(): FavouritesSubComponent
    fun profileSubComponent(): ProfileSubComponent
    fun hotelSubComponent(): HotelSubComponent

    fun inject(mainActivity: MainActivity)
    fun inject(mainPresenter: MainPresenter)

//    fun inject(slideShowAdapter: SlideShowAdapter)

//    fun inject(usersPresenter: UsersPresenter)
//    fun inject(repositoryPresenter: RepositoryPresenter)
//    fun inject(profilePresenter: ProfilePresenter)

}