package com.example.touristagency.mvp.presenter

import com.example.touristagency.mvp.view.ToursView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import javax.inject.Inject

class ToursMainPresenter : MvpPresenter<ToursView>() {


    @Inject
    lateinit var router: Router
    @Inject
    lateinit var screens: IScreens

//    class UsersListPresenter : IUserListPresenter {
//
//        val users = mutableListOf<GithubUser>()
//
//        override var itemClickListener: ((UserItemView) -> Unit)? = null
//        override fun getCount() = users.size
//        override fun bindView(view: UserItemView) {
//            val user = users[view.pos]
//            user.login?.let { view.setLogin(it) }
//            user.avatarUrl?.let { view.loadAvatar(it) }
//
//        }
//    }

//    val usersListPresenter = UsersListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.init()
//        loadData()
//        usersListPresenter.itemClickListener = { itemView ->
//            val user = usersListPresenter.users[itemView.pos]
//
//            router.navigateTo(screens.profileUser(user)) // переход на экран пользователя c помощью router.navigateTo
//        }
    }

//    fun loadData() {
//        usersRepo.getUsers().observeOn(AndroidSchedulers.mainThread()).subscribe({ repos ->
//            usersListPresenter.users.clear()
//            usersListPresenter.users.addAll(repos)
//            viewState.updateList()
//
//        }, {
//            println("Error: ${it.message}")
//
//        })
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