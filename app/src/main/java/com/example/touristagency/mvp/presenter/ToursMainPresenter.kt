package com.example.touristagency.mvp.presenter

import android.util.Log
import android.view.MenuItem
import com.example.touristagency.mvp.model.Tour
import com.example.touristagency.mvp.model.users.IGithubUsersRepo
import com.example.touristagency.mvp.presenter.list.IUserListPresenter
import com.example.touristagency.mvp.view.ToursView
import com.example.touristagency.mvp.view.list.UserItemView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpPresenter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ToursMainPresenter : MvpPresenter<ToursView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens


    @Inject
    lateinit var usersRepo: IGithubUsersRepo


    private val currentCurrency: String = "₽"// текущая валюта
    private lateinit var cities: Array<String>
    private val minimumNumberOfNights = 1
    private val maximumNumberOfNights = 30
    private val minimumNumberOfPeople: Int = 1
    private val maximumNumberOfPeople: Int = 10

    val calendar = Calendar.getInstance()

    private val sortingStrings = listOf("Рекомендуемое", "По рейтингу", "Дешевле", "Дороже") // способы сортировки для меню сортировки

    private var savedValuesFiltersDialog = mutableMapOf<String, String>() // сохраненные значения для основных view в меню фильтров
    private val priceNumberFromKey = "priceNumberFrom" // ключ для сохранения "цены от" в map
    private val priceNumberToKey = "priceNumberTo" // ключ для сохранения "цены до" в map
    private val starsNumberFromKey = "starsNumberFrom" // ключ для сохранения "колво звезд от" в map
    private val starsNumberToKey = "starsNumberTo" // ключ для сохранения  "колво звезд до" в map
    private val foodTypesRadioGroupKey = "foodTypesRadioGroup" // ключ для сохранения checked элемента в radiogroup типов питания
    private val foodSystemsRadioGroupKey = "foodSystemsRadioGroup" // ключ для сохранения checked элемента в radiogroup систем питания
    private val infrastructureCheckBox1Key = "infrastructureCheckBox1" // ключ для сохранения состояния чекбокса 1
    private val infrastructureCheckBox2Key = "infrastructureCheckBox2" // ключ для сохранения состояния чекбокса 2
    private val infrastructureCheckBox3Key = "infrastructureCheckBox3" // ключ для сохранения состояния чекбокса 3
    private val infrastructureCheckBox4Key = "infrastructureCheckBox4" // ключ для сохранения состояния чекбокса 4
    private val infrastructureCheckBox5Key = "infrastructureCheckBox5" // ключ для сохранения состояния чекбокса 5
    private val infrastructureCheckBox6Key = "infrastructureCheckBox6" // ключ для сохранения состояния чекбокса 6
    private val infrastructureCheckBox7Key = "infrastructureCheckBox7" // ключ для сохранения состояния чекбокса 7

    private var savedValuesCityDialog = mutableMapOf<String, String>() // сохраненные значения для основных view в cityDialog
    private val cityNameKeyCityDialog = "cityName" // ключ для сохранения города в map
    private val dateKeyCityDialog = "date" // ключ для сохранения даты вылета в map
    private val cityDepartureNameKeyCityDialog = "cityDeparture" // ключ для сохранения города вылета в map
    private val nightsKeyCityDialog = "nights" // ключ для сохранения количества ночей в map
    private val peoplesKeyCityDialog = "peoples" // ключ для сохранения количества людей в map


    class ToursListPresenter : IUserListPresenter {

        val users = mutableListOf<Tour>()

        override var itemClickListener: ((UserItemView) -> Unit)? = null
        override fun getCount() = users.size
        override fun bindView(view: UserItemView) {
            val user = users[view.pos]
            user.login?.let { view.setLogin(it) }
            user.avatarUrl?.let { view.loadAvatar(it) }

        }
    }

    val toursListPresenter = ToursListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.initCurrentCurrency(currentCurrency)
        viewState.getCitiesArrayFromResourses()
        viewState.setCitiesList(cities)
        initMinDate()
        viewState.setMinimumNumberNights(minimumNumberOfNights)
        viewState.setMaximumNumberNights(maximumNumberOfNights)
        viewState.setMinimumNumberPeoples(minimumNumberOfPeople)
        viewState.setMaximumNumberPeoples(maximumNumberOfPeople)

        viewState.initBottomNavigationMenu()


        viewState.init()
//        loadData()
        toursListPresenter.itemClickListener = { itemView ->
            val user = toursListPresenter.users[itemView.pos]

//            router.navigateTo(screens.profileUser(user)) // переход на экран пользователя c помощью router.navigateTo
        }


        initViews()

    }

    private fun initViews() {
        viewState.setTextSortingButton(sortingStrings[0]) // присвоение дефолтного способа сортировки


        viewState.initCityDialog()
        viewState.initCityAndNightsButton()
        viewState.initFindButtonCityDialog()
        setInitialCityValues()
        viewState.updateCityButton(savedValuesCityDialog[cityNameKeyCityDialog].toString(), savedValuesCityDialog[nightsKeyCityDialog].toString())


        initFiltersDialogFun()

        viewState.initToursButton()
        viewState.initHotelsButton()

        viewState.initSortingButton()
        viewState.initFiltersButton()

        viewState.testInitFirstRecyclerItem()
        viewState.testInitSecondRecyclerItem()
        viewState.testInitThirdRecyclerItem()
    }

    fun navigationSearchOnClick() {
        router.replaceScreen(screens.mainAllTours())
    }

    fun navigationHotToursOnClick() {
        router.replaceScreen(screens.hotTours())
    }

    fun navigationFavouriteOnClick() {
        router.replaceScreen(screens.favourites())
    }

    private fun initMinDate() {

        calendar.add(Calendar.DAY_OF_MONTH, 1) // минимальная дата +1 (завтра)
        val minYear = calendar.get(Calendar.YEAR)
        val minMonth = calendar.get(Calendar.MONTH)
        val minDay = calendar.get(Calendar.DAY_OF_MONTH)
        viewState.initMinDate(calendar, minYear, minMonth, minDay)

    }

    fun filtersButtonOnClick() {
        viewState.showFiltersDialog()
    }

    fun toursButtonOnClick() {
        viewState.switchStateButtonsToursAndHotels(true)
    }

    fun hotelsButtonOnClick() {
        // TODO: РАЗОБРАТЬСЯ С МЕСТОМ ИНИЦИАЦИИ ЗАПРОСА
        Log.e("=========== ", "HERE")
        loadData()




        viewState.switchStateButtonsToursAndHotels(false)

    }


    private fun loadData() {

        usersRepo.getUsers().observeOn(AndroidSchedulers.mainThread()).subscribe({ repos ->
            Log.e("-------------- ", repos.toString())
//            usersListPresenter.users.clear()
//            usersListPresenter.users.addAll(repos)
//            viewState.updateList()

        }, {
            println("Error: ${it.message}")

        })

    }


    fun cityAndNightsButtonOnClick() {
        viewState.showCityDialog()
    }

    fun sortingButtonOnClick() {
        viewState.initSortingMenu(sortingStrings)
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

    fun priceSliderOnChange(value1: String, value2: String) {
        viewState.setValueToPriceNumberFrom(value1 + currentCurrency)
        viewState.setValueToPriceNumberTo(value2 + currentCurrency)
    }

    fun priceStarsOnChange(value1: String, value2: String) {
        viewState.setValueToStarsNumberFrom(value1)
        viewState.setValueToStarsNumberTo(value2)
    }

    private fun setCurrentValuesFiltersDialog() {
        viewState.setValuesFiltersDialog(
            savedValuesFiltersDialog[priceNumberFromKey].toString(),
            savedValuesFiltersDialog[priceNumberToKey].toString(),
            savedValuesFiltersDialog[starsNumberFromKey].toString(),
            savedValuesFiltersDialog[starsNumberToKey].toString(),
            savedValuesFiltersDialog[foodTypesRadioGroupKey].toString(),
            savedValuesFiltersDialog[foodSystemsRadioGroupKey].toString(),
            savedValuesFiltersDialog[infrastructureCheckBox1Key].toBoolean(),
            savedValuesFiltersDialog[infrastructureCheckBox2Key].toBoolean(),
            savedValuesFiltersDialog[infrastructureCheckBox3Key].toBoolean(),
            savedValuesFiltersDialog[infrastructureCheckBox4Key].toBoolean(),
            savedValuesFiltersDialog[infrastructureCheckBox5Key].toBoolean(),
            savedValuesFiltersDialog[infrastructureCheckBox6Key].toBoolean(),
            savedValuesFiltersDialog[infrastructureCheckBox7Key].toBoolean()
        )
    }

    fun cancelButtonFiltersOnClick() {
        setCurrentValuesFiltersDialog()
        viewState.cancelFiltersDialog()
    }

    fun clearButtonFiltersOnClick() {
        setInitialFiltersValues()
        setCurrentValuesFiltersDialog()
    }

    fun filtersDialogOnCancel(list: MutableMap<String, String>) {
        savedValuesFiltersDialog = list

        //TODO тут сделать запрос в базу по выбранным фильтрам
    }

    private fun initFiltersDialogFun() {
        viewState.initFiltersDialog()

        setInitialFiltersValues()
        setCurrentValuesFiltersDialog()
    }

    private fun setInitialFiltersValues() {
        savedValuesFiltersDialog.put(priceNumberFromKey, "0")
        savedValuesFiltersDialog.put(priceNumberToKey, "1000000")

        savedValuesFiltersDialog.put(starsNumberFromKey, "1")
        savedValuesFiltersDialog.put(starsNumberToKey, "5")

        savedValuesFiltersDialog.put(
            foodTypesRadioGroupKey,
            "food_types_radiobutton_1"
        )

        savedValuesFiltersDialog.put(
            foodSystemsRadioGroupKey,
            "food_systems_radiobutton_1"
        )

        savedValuesFiltersDialog.put(infrastructureCheckBox1Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox2Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox3Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox4Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox5Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox6Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox7Key, false.toString())

    }

    fun cancelButtonCityDialogOnClick() {
        viewState.cancelCityDialog()
    }

    fun toursButtonCityDialogOnClick() {
        viewState.switchStateButtonsToursAndHotels(true)
    }

    fun hotelsButtonCityDialogOnClick() {
        viewState.switchStateButtonsToursAndHotels(false)
    }


    fun setCities(cities: Array<String>) {
        this.cities = cities
    }

    fun findButtonCityDialogOnClick() {
//        saveValuesForCityDialog()
        viewState.dismissCityDialog()
        setCurrentValuesCityDialog()
        viewState.updateCityButton(savedValuesCityDialog[cityNameKeyCityDialog].toString(), savedValuesCityDialog[nightsKeyCityDialog].toString())
    }

    fun saveValuesCityDialog(savedValuesCityDialog: MutableMap<String, String>) {
        this.savedValuesCityDialog = savedValuesCityDialog
    }

    private fun setCurrentValuesCityDialog() {
        viewState.setValuesCityDialog(
            savedValuesCityDialog[cityNameKeyCityDialog].toString(),
            savedValuesCityDialog[dateKeyCityDialog].toString(),
            savedValuesCityDialog[cityDepartureNameKeyCityDialog].toString(),
            savedValuesCityDialog[nightsKeyCityDialog].toString(),
            savedValuesCityDialog[peoplesKeyCityDialog].toString()
        )
    }

    private fun setInitialCityValues() {
        savedValuesCityDialog.put(cityNameKeyCityDialog, "")
        savedValuesCityDialog.put(dateKeyCityDialog, getDateOfDeparture())
        savedValuesCityDialog.put(cityDepartureNameKeyCityDialog, "")
        savedValuesCityDialog.put(nightsKeyCityDialog, "10")
        savedValuesCityDialog.put(peoplesKeyCityDialog, "1")


    }

    fun getDateOfDeparture() = formatDate(calendar.time)

    fun formatDate(date: Date?): String { // формат даты для выбора числа вылета
        val format = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return format.format(date)
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