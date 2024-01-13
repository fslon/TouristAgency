package com.example.touristagency.mvp.presenter

import android.view.MenuItem
import com.example.touristagency.mvp.model.tours.Tour
import com.example.touristagency.mvp.presenter.list.ITourListPresenter
import com.example.touristagency.mvp.view.FavouritesView
import com.example.touristagency.mvp.view.HotToursView
import com.example.touristagency.mvp.view.list.TourItemView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import javax.inject.Inject

class FavouritesPresenter(val favouriteTours: MutableList<Tour>) : MvpPresenter<FavouritesView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    private val currentCurrency: String = "₽"// текущая валюта

    private val sortingStrings = listOf("Рекомендуемое", "По рейтингу", "Дешевле", "Дороже") // способы сортировки для меню сортировки

    class FavouriteToursListPresenter(val favouriteTours: MutableList<Tour>) : ITourListPresenter {
        override var itemClickListener: ((TourItemView) -> Unit)? = null
        override var favouriteButtonClickListener: ((TourItemView) -> Unit)? = null
        override fun getCount() = favouriteTours.size
        override fun bindView(view: TourItemView) {
            val tour = favouriteTours[view.pos]
            tour.place?.let { view.setPlace(it) }
            tour.price?.let { view.setPrice(it) }
            tour.airportDistance?.let { view.setAirportDistance(it) }
            tour.beachDistance?.let { view.setBeachDistance(it) }
            tour.rating?.let { view.setRating(it) }
            tour.parking?.let { view.setParking(it) }
            tour.stars?.let { view.setStars(it) }
            tour.foodSystem?.let { view.setFoodSystem(it) }
            tour.foodSystem?.let { view.setFoodSystem(it) }
            tour.foodType?.let { view.setFoodType(it) }

            tour.photo1?.let { view.loadPicture1(it) }
            tour.photo2?.let { view.loadPicture2(it) }
            tour.photo3?.let { view.loadPicture3(it) }
        }
    }

    val toursListPresenter = FavouriteToursListPresenter(favouriteTours)


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.initCurrentCurrency(currentCurrency)

        viewState.initBottomNavigationMenu()

        viewState.setTextSortingButton(sortingStrings[0]) // присвоение дефолтного способа сортировки
        viewState.initSortingButton()


        viewState.init()
        toursListPresenter.itemClickListener = { itemView ->
            val tour = toursListPresenter.favouriteTours[itemView.pos]

            router.navigateTo(screens.tour(tour))
//            router.navigateTo(screens.profileUser(user)) // переход на экран пользователя c помощью router.navigateTo
        }

//        toursListPresenter.favouriteButtonClickListener = { itemView ->
//            updateImage(itemView.pos)
//            favouriteTours.add(toursListPresenter.favouriteTours[itemView.pos]) // добавление текущего тура в любимые
//        }


    }


    private fun updateImage(position: Int) {
        viewState.updateImage(position)
    }


    fun sortingButtonOnClick() {
        viewState.initSortingMenu(sortingStrings)
    }

    fun navigationSearchOnClick() {
        router.replaceScreen(screens.mainAllTours())
    }

    fun navigationHotToursOnClick() {
//        router.replaceScreen(screens.hotTours(favouriteTours))
    }

    fun navigationFavouriteOnClick() {
//        router.replaceScreen(screens.favourites())
    }

    fun navigationProfileOnClick() {
//        router.replaceScreen(screens.profile(favouriteTours))
    }

    fun sortingItemOnClick(item: MenuItem) {
        when (item.title) {
            sortingStrings[0] -> { // Рекомендуемое
                // TODO Обработка выбранной сортировки
                viewState.setTextSortingButton(item.title.toString())
            }

            sortingStrings[1] -> { // Сначала новое
                // TODO Обработка выбранной сортировки
                viewState.setTextSortingButton(item.title.toString())
            }

            sortingStrings[2] -> { // Дешевле
                // TODO Обработка выбранной сортировки
                viewState.setTextSortingButton(item.title.toString())
            }

            sortingStrings[3] -> { // Дороже
                // TODO Обработка выбранной сортировки
                viewState.setTextSortingButton(item.title.toString())
            }
        }
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