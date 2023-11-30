package com.example.touristagency.mvp.presenter

import android.view.MenuItem
import com.example.touristagency.mvp.view.FavouritesView
import com.example.touristagency.mvp.view.HotToursView
import com.example.touristagency.mvp.view.ProfileView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
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

//    private val sortingStrings = listOf("Рекомендуемое", "По рейтингу", "Дешевле", "Дороже") // способы сортировки для меню сортировки

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

        viewState.showSnacks()
        viewState.showSnacksRegistration()

//        viewState.setTextSortingButton(sortingStrings[0]) // присвоение дефолтного способа сортировки
//        viewState.initSortingButton()
//
//        viewState.testInitFirstRecyclerItem()
//        viewState.testInitSecondRecyclerItem()
//        viewState.testInitThirdRecyclerItem()

    }
//
//    fun sortingButtonOnClick() {
//        viewState.initSortingMenu(sortingStrings)
//    }

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

//    fun sortingItemOnClick(item: MenuItem) {
//        when (item.title) {
//            sortingStrings[0] -> { // Рекомендуемое
//                // TODO Обработка выбранной сортировки
//                viewState.setTextSortingButton(item.title.toString())
//            }
//
//            sortingStrings[1] -> { // Сначала новое
//                // TODO Обработка выбранной сортировки
//                viewState.setTextSortingButton(item.title.toString())
//            }
//
//            sortingStrings[2] -> { // Дешевле
//                // TODO Обработка выбранной сортировки
//                viewState.setTextSortingButton(item.title.toString())
//            }
//
//            sortingStrings[3] -> { // Дороже
//                // TODO Обработка выбранной сортировки
//                viewState.setTextSortingButton(item.title.toString())
//            }
//        }
//    }



    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewState.release()
    }
}