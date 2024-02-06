package com.example.touristagency.mvp.presenter

import android.util.Log
import com.example.touristagency.mvp.model.cities.ICitiesRepo
import com.example.touristagency.mvp.model.hotels.IHotelsRepo
import com.example.touristagency.mvp.model.hotels.Hotel
import com.example.touristagency.mvp.presenter.list.IHotelListPresenter
import com.example.touristagency.mvp.view.HotelsView
import com.example.touristagency.mvp.view.list.HotelItemView
import com.example.touristagency.navigation.IScreens
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class HotelsMainPresenter : MvpPresenter<HotelsView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    @Inject
    lateinit var toursRepo: IHotelsRepo

    @Inject
    lateinit var citiesRepo: ICitiesRepo

    private val compositeDisposable = CompositeDisposable()

    private val currentCurrency: String = "₽"// текущая валюта

    private var cities = mutableListOf<String>()

    private val minimumNumberOfNights = 1
    private val maximumNumberOfNights = 30
    private val minimumNumberOfPeople: Int = 1
    private val maximumNumberOfPeople: Int = 10

    val calendar = Calendar.getInstance()

    private val sortingStrings = listOf("Рекомендуемое", "По рейтингу", "Дешевле", "Дороже") // способы сортировки для меню сортировки
    private var currentSortingValue = sortingStrings[0] // текущий выбранный способ сортировки (по дефолту нулевой)

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

    //    private val cityDepartureNameKeyCityDialog = "cityDeparture" // ключ для сохранения города вылета в map
    private val nightsKeyCityDialog = "nights" // ключ для сохранения количества ночей в map
    private val peoplesKeyCityDialog = "peoples" // ключ для сохранения количества людей в map

    val favouriteTours = mutableListOf<Hotel>()
//    val hotTours = mutableListOf<Tour>()

    class ToursListPresenter : IHotelListPresenter {

        val tours = mutableListOf<Hotel>()

        override var itemClickListener: ((HotelItemView) -> Unit)? = null
        override var favouriteButtonClickListener: ((HotelItemView) -> Unit)? = null
        override fun getCount() = tours.size
        override fun bindView(view: HotelItemView) {
            val tour = tours[view.pos]

            Log.e("+++++ ", tour.toString())

            tour.name?.let { view.setName(it) }
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

            val pictures = mutableListOf<String>()
            pictures.add(tour.photo1.toString())
            pictures.add(tour.photo2.toString())
            pictures.add(tour.photo3.toString())
            view.loadPictures(pictures)

            tour.wifi?.let { view.setWifi(it) }
            tour.lineNumber?.let { view.setLine(it) }

        }
    }

    val toursListPresenter = ToursListPresenter()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.initCurrentCurrency(currentCurrency)

        loadCities()
        viewState.setCitiesList(cities)

        initMinDate()
        viewState.setMinimumNumberNights(minimumNumberOfNights)
        viewState.setMaximumNumberNights(maximumNumberOfNights)
        viewState.setMinimumNumberPeoples(minimumNumberOfPeople)
        viewState.setMaximumNumberPeoples(maximumNumberOfPeople)

        viewState.initBottomNavigationMenu()


        viewState.init()

        loadTours()
        toursListPresenter.itemClickListener = { itemView ->
            val tour = toursListPresenter.tours[itemView.pos]

            router.navigateTo(screens.hotel(tour))
//            router.navigateTo(screens.profileUser(user)) // переход на экран пользователя c помощью router.navigateTo
        }

        toursListPresenter.favouriteButtonClickListener = { itemView ->
            updateImage(itemView.pos)

            favouriteTours.add(toursListPresenter.tours[itemView.pos]) // добавление текущего тура в любимые
        }


        initViews()

    }

    fun onRefreshAction() { // метод, используемый при вытягивании сверху обновления страницы
        loadCities()

        if (savedValuesCityDialog[cityNameKeyCityDialog]?.isNotBlank() == true and (savedValuesCityDialog[cityNameKeyCityDialog]?.isNotEmpty() == true)) {
            savedValuesCityDialog[cityNameKeyCityDialog]?.let { loadHotelsByCity(it) }
        } else loadTours()
    }


    private fun updateImage(position: Int) {
        viewState.updateImage(position)
    }

    private fun initViews() {
        viewState.setTextSortingButton(currentSortingValue) // присвоение дефолтного способа сортировки


        viewState.initCityDialog()
        viewState.initCityAndNightsButton()
        viewState.initFindButtonCityDialog()
        setInitialCityValues()
        viewState.updateCityButton(savedValuesCityDialog[cityNameKeyCityDialog].toString(), savedValuesCityDialog[nightsKeyCityDialog].toString())


        initFiltersDialogFun()

        viewState.initSortingButton()
        viewState.initFiltersButton()

    }

//    fun openTourFragment() {
//        router.navigateTo(screens.tour())
//    }

    fun navigationSearchOnClick() {
        router.replaceScreen(screens.mainAllHotels())
    }

    fun navigationHotToursOnClick() {
//        router.replaceScreen(screens.hotTours(hotTours))
    }

    fun navigationFavouriteOnClick() {
//        router.replaceScreen(screens.favourites(favouriteTours))
    }

    fun navigationProfileOnClick() {
//        router.replaceScreen(screens.profile(favouriteTours))
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

    private fun loadCities() { // получение списка городов

        val disposableCities = citiesRepo.getCities().observeOn(AndroidSchedulers.mainThread()).subscribe({ repos ->
            Log.e("--------------- ", repos.toString())

            for (city in repos) {
                cities.add(city.cityName.toString())
            }

        }, {
            println("Error: ${it.message}")
//            viewState.setVisibilityNotFoundLayout(true)
//            viewState.setVisibilityRecyclerView(false)
            viewState.showSnackbar("Произошла ошибка при получении данных")
        })
        compositeDisposable.add(disposableCities)
    }


    private fun loadTours() { // получение списка туров
        val disposableTours = toursRepo.getHotels().observeOn(AndroidSchedulers.mainThread()).subscribe({ repos ->

            Log.e("--------------- ", repos.toString())

            toursListPresenter.tours.clear()
            toursListPresenter.tours.addAll(repos)

            sortingItemOnClick(currentSortingValue) // для применения текущей сортировки

            viewState.updateList()

            showOrHideNotFoundLayout()

            viewState.stopRefreshing()
        }, {
            println("Error: ${it.message}")

            viewState.setVisibilityNotFoundLayout(true)
            viewState.setVisibilityRecyclerView(false)
            viewState.showSnackbar("Произошла ошибка при получении данных")

            viewState.stopRefreshing()
        })
        compositeDisposable.add(disposableTours)

    }


    private fun loadHotelsByCity(cityName: String) { // получение списка туров
        val disposableHotelsByCity = toursRepo.getHotelsByCity(cityName).observeOn(AndroidSchedulers.mainThread()).subscribe({ repos ->

            Log.e("!!!!!!!!! ", repos.toString())

            toursListPresenter.tours.clear()
            toursListPresenter.tours.addAll(repos)

            sortingItemOnClick(currentSortingValue) // для применения текущей сортировки

            viewState.updateList()

            showOrHideNotFoundLayout()

            viewState.stopRefreshing()
        }, {
            println("Error: ${it.message}")

            viewState.setVisibilityNotFoundLayout(true)
            viewState.setVisibilityRecyclerView(false)
            viewState.showSnackbar("Произошла ошибка при получении данных")

            viewState.stopRefreshing()
        })
        compositeDisposable.add(disposableHotelsByCity)

    }

    private fun showOrHideNotFoundLayout() { // скрывает или показывает "ничего не найдено" в зависимости от наполнения массива с отелями
        if (toursListPresenter.getCount() == 0) {
            viewState.setVisibilityNotFoundLayout(true)
            viewState.setVisibilityRecyclerView(false)
        } else {
            viewState.setVisibilityNotFoundLayout(false)
            viewState.setVisibilityRecyclerView(true)
        }
    }


    fun cityAndNightsButtonOnClick() {
        viewState.showCityDialog()
    }

    fun sortingButtonOnClick() {
        viewState.initSortingMenu(sortingStrings)
    }

    fun sortingItemOnClick(titleOfItem: String) {
        when (titleOfItem) { // может не работать, если названия кнопок в макете будут не совпадать с названиями способов сортировки в массиве (например если язык поменять)
            sortingStrings[0] -> { // Рекомендуемое
                // TODO Обработка выбранной сортировки
                currentSortingValue = titleOfItem

                viewState.setTextSortingButton(titleOfItem)

                toursListPresenter.tours.sortBy { it.id?.toInt() }
            }

            sortingStrings[1] -> { // По рейтингу
                // TODO Обработка выбранной сортировки
                currentSortingValue = titleOfItem

                viewState.setTextSortingButton(titleOfItem)

                toursListPresenter.tours.sortByDescending { it.rating?.toInt() }
            }

            sortingStrings[2] -> { // Дешевле
                // TODO Обработка выбранной сортировки
                currentSortingValue = titleOfItem

                viewState.setTextSortingButton(titleOfItem)

                toursListPresenter.tours.sortBy { it.price?.toInt() }

            }

            sortingStrings[3] -> { // Дороже
                // TODO Обработка выбранной сортировки
                currentSortingValue = titleOfItem

                viewState.setTextSortingButton(titleOfItem)

                toursListPresenter.tours.sortByDescending { it.price?.toInt() }
            }
        }
        viewState.updateList()
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

    fun findButtonCityDialogOnClick(
        cityFieldText: String,
        dateFieldText: String,
        nightsFieldText: String,
        peoplesFieldText: String
    ) { // вызывается при нажатии кнопки "Найти отели", принимает данные из полей меню выбора города, обрабаывает их
//        saveValuesForCityDialog()

        if (isSelectedCitiesValid(cityFieldText)) {
            saveValuesForCityDialog(cityFieldText, dateFieldText, nightsFieldText, peoplesFieldText)

            viewState.dismissCityDialog()
            setCurrentValuesCityDialog()
            viewState.updateCityButton(cityFieldText, nightsFieldText)

            loadHotelsByCity(cityFieldText)

        }


    }


    private fun saveValuesForCityDialog(
        cityFieldText: String,
        dateFieldText: String,
        nightsFieldText: String,
        peoplesFieldText: String
    ) { // сохранение значений из основных view из cityDialog в map
        savedValuesCityDialog[cityNameKeyCityDialog] = cityFieldText
        savedValuesCityDialog[dateKeyCityDialog] = dateFieldText
        savedValuesCityDialog[nightsKeyCityDialog] = nightsFieldText
        savedValuesCityDialog[peoplesKeyCityDialog] = peoplesFieldText
    }

    private fun isSelectedCitiesValid(cityFieldText: String): Boolean { // проверка на валидность поля с выбором города с городами
        if (cityFieldText.isBlank()
        ) return true // если поле города назначения пустое, то пропускаем, будет "Россия"

        for (item in cities) { // проверка, есть ли в списке городов город назначения
            if (item == cityFieldText) return true
        }

        viewState.showSnackBarAboutValueCityDialog("Некорректное название города, пожалуйста, выберите из выпадающего списка")
        return false
    }


//    fun saveValuesCityDialog(savedValuesCityDialog: MutableMap<String, String>) {
//        this.savedValuesCityDialog = savedValuesCityDialog
//    }

    private fun setCurrentValuesCityDialog() {
        viewState.setValuesCityDialog(
            savedValuesCityDialog[cityNameKeyCityDialog].toString(),
            savedValuesCityDialog[dateKeyCityDialog].toString(),
            savedValuesCityDialog[nightsKeyCityDialog].toString(),
            savedValuesCityDialog[peoplesKeyCityDialog].toString()
        )
    }

    private fun setInitialCityValues() {
        savedValuesCityDialog.put(cityNameKeyCityDialog, "")
        savedValuesCityDialog.put(dateKeyCityDialog, getDateOfDeparture())
        savedValuesCityDialog.put(nightsKeyCityDialog, "10")
        savedValuesCityDialog.put(peoplesKeyCityDialog, "1")
    }

    fun getDateOfDeparture() = formatDate(calendar.time)

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
        compositeDisposable.dispose()
    }
}