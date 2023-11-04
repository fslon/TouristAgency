package com.example.touristagency.mvp.presenter

import com.example.touristagency.mvp.view.MainView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import javax.inject.Inject

class MainPresenter() : MvpPresenter<MainView>() {

    @Inject lateinit var router: Router
    @Inject lateinit var screens: IScreens

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(screens.mainAllTours())
    }
    fun backClicked() {
        router.exit()
    }
}
