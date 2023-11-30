package com.example.touristagency.mvp.presenter

import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import com.example.touristagency.mvp.view.HotToursView
import com.example.touristagency.mvp.view.TourView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import javax.inject.Inject

class TourPresenter : MvpPresenter<TourView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    private val currentCurrency: String = "₽"// текущая валюта

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

        viewState.initCurrentCurrency(currentCurrency)

        viewState.testInitFirstRecyclerItem()
    }

    fun buyTourButtonOnClick(){
        // создаем намерение для открытия приложения звонки
        val intent = Intent(Intent.ACTION_DIAL)

        // набираем определенный номер
        intent.data = Uri.parse("tel:100")

        viewState.startCall(intent)
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