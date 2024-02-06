package com.example.touristagency.mvp.presenter

import android.view.MenuItem
import com.example.touristagency.mvp.model.hotels.Hotel
import com.example.touristagency.mvp.presenter.list.IHotelListPresenter
import com.example.touristagency.mvp.view.HotHotelsView
import com.example.touristagency.mvp.view.list.HotelItemView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import javax.inject.Inject

class HotHotelsPresenter(val hotTours: MutableList<Hotel>) : MvpPresenter<HotHotelsView>() { // todo убрать получение массива с турами
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    private val currentCurrency: String = "₽"// текущая валюта

    private val sortingStrings = listOf("Рекомендуемое", "По рейтингу", "Дешевле", "Дороже") // способы сортировки для меню сортировки

    class HotToursListPresenter(val hotTours: MutableList<Hotel>) : IHotelListPresenter {
        override var itemClickListener: ((HotelItemView) -> Unit)? = null
        override var favouriteButtonClickListener: ((HotelItemView) -> Unit)? = null
        override fun getCount() = hotTours.size
        override fun bindView(view: HotelItemView) {
            val tour = hotTours[view.pos]
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

//            tour.photo1?.let { view.loadPicture1(it) }
//            tour.photo2?.let { view.loadPicture2(it) }
//            tour.photo3?.let { view.loadPicture3(it) }
        }
    }

    val toursListPresenter = HotToursListPresenter(hotTours)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.initCurrentCurrency(currentCurrency)

        viewState.initBottomNavigationMenu()

        viewState.setTextSortingButton(sortingStrings[0]) // присвоение дефолтного способа сортировки
        viewState.initSortingButton()

        viewState.init()
        toursListPresenter.itemClickListener = { itemView ->
            val tour = toursListPresenter.hotTours[itemView.pos]

            router.navigateTo(screens.hotel(tour))
//            router.navigateTo(screens.profileUser(user)) // переход на экран пользователя c помощью router.navigateTo
        }


//        viewState.testInitFirstRecyclerItem()
//        viewState.testInitSecondRecyclerItem()
//        viewState.testInitThirdRecyclerItem()

    }

    private fun updateImage(position: Int) {
        viewState.updateImage(position)
    }


    fun sortingButtonOnClick() {
        viewState.initSortingMenu(sortingStrings)
    }

    fun navigationSearchOnClick() {
        router.replaceScreen(screens.mainAllHotels())
    }

    fun navigationHotToursOnClick() {
//        router.replaceScreen(screens.hotTours())
    }

    fun navigationFavouriteOnClick() {
        router.replaceScreen(screens.favourites(hotTours))
    }

    fun navigationProfileOnClick() {
//        router.replaceScreen(screens.profile())
    }

    fun sortingItemOnClick(item: MenuItem) { // todo убрать отсюда item, view не должно быть в presenter
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