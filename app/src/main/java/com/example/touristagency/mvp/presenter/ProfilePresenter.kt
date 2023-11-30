package com.example.touristagency.mvp.presenter

import com.example.touristagency.mvp.view.ProfileView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import moxy.MvpPresenter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ProfilePresenter : MvpPresenter<ProfileView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

//    private val currentCurrency: String = "₽"// текущая валюта


//    class ToursListPresenter : IUserListPresenter {
//        val users = mutableListOf<Tour>()
//        override var itemClickListener: ((UserItemView) -> Unit)? = null
//        override fun getCount() = users.size
//        override fun bindView(view: UserItemView) {
//            val user = users[view.pos]
//            user.login?.let { view.setLogin(it) }
//            user.avatarUrl?.let { view.loadAvatar(it) }
//        }
//    }
//    val toursListPresenter = ToursListPresenter()
//

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

//        viewState.initCurrentCurrency(currentCurrency)

        viewState.initBottomNavigationMenu()

        viewState.initDatePicker()

        viewState.showLoginSnacks()
        viewState.initLoginButton()

    }


    fun loginButtonOnClick(login: String, password: String) {
        //todo вызов




        if (!isRegistered) {
            Snackbar.make(binding.container, "Вы успешно зарегистрировались", Snackbar.LENGTH_SHORT).show()
            switchIsRegisteredText(true)
        } else {
//               Snackbar.make(binding.container, "Вы успешно вошли в аккаунт", Snackbar.LENGTH_SHORT).show()
            switchIsRegisteredText(false)
        }
        isRegistered = !isRegistered


    }

    fun registerButtonOnClick(login: String, password: String, email: String, birthdate: String, name: String, lastname: String, surname: String) {

    }


    //
    //
    //
    //
    //
    fun navigationSearchOnClick() {
        router.replaceScreen(screens.mainAllTours())
    }

    fun navigationHotToursOnClick() {
        router.replaceScreen(screens.hotTours())
    }

    fun navigationFavouriteOnClick() {
        router.replaceScreen(screens.favourites())
    }

    fun navigationProfileOnClick() {
        router.replaceScreen(screens.profile())
    }


    fun formatDate(date: Date?): String { // формат даты для выбора числа вылета
        val format = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewState.release()
    }
}