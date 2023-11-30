package com.example.touristagency.mvp.presenter

import com.example.touristagency.mvp.model.users.IDataSourceUser
import com.example.touristagency.mvp.view.ProfileView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ProfilePresenter : MvpPresenter<ProfileView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    @Inject
    lateinit var usersRepo: IDataSourceUser

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

        viewState.initLoginButton()

    }

    private fun loginQuery(login: String, password: String) {
        usersRepo.loginUser(login, password).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) { // Обработка успешного ответа
                    val responseData = response.body()
                    val token = responseData

                    viewState.switchIsRegisteredText(true)
                    viewState.showSnack("Вы успешно вошли в аккаунт")

                } else { // Обработка ошибочного ответа
                    viewState.showSnack("Неправильное имя пользователя или пароль")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) { // Обработка ошибки
                viewState.showSnack("Ошибка доступа к серверу")
            }
        })
    }

    private fun registerQuery(login: String, password: String, email: String, birthdate: String, name: String, lastname: String, surname: String) {
        usersRepo.registerUser(login, password, email, birthdate, name, lastname, surname).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) { // Обработка успешного ответа
                    val responseData = response.body()
                    val token = responseData

                    viewState.switchIsRegisteredText(true)
                    viewState.showSnack("Вы успешно зарегистрировали аккаунт")

                } else { // Обработка ошибочного ответа
                    viewState.showSnack("Такой пользователь уже существует")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) { // Обработка ошибки
                viewState.showSnack("Ошибка доступа к серверу")
            }
        })
    }


    fun loginButtonOnClick(login: String, password: String) {
        loginQuery(login, password)
    }

    fun registerButtonOnClick(login: String, password: String, email: String, birthdate: String, name: String, lastname: String, surname: String) {
        registerQuery(login, password, email, birthdate, name, lastname, surname)
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