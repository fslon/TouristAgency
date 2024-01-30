package com.example.touristagency.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.subComponents.ToursSubComponent
import com.example.touristagency.databinding.FragmentToursMainBinding
import com.example.touristagency.mvp.presenter.ToursMainPresenter
import com.example.touristagency.mvp.view.ToursView
import com.example.touristagency.ui.activity.BackButtonListener
import com.example.touristagency.ui.adapter.ToursRVAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ToursMainFragment : MvpAppCompatFragment(), ToursView, BackButtonListener {
    private var _binding: FragmentToursMainBinding? = null
    private val binding get() = _binding!!

    private var toursSubComponent: ToursSubComponent? = null


    private lateinit var cityDialog: Dialog // диалог с выбором города и даты
    private lateinit var findButton: MaterialButton // кнопка "Найти туры"/"Найти  отели" в cityDialog
    private lateinit var filtersDialog: BottomSheetDialog // диалог с фильтрами
    private lateinit var citySheet: View // layout для диалога с выбором города и даты (cityDialog)

    private lateinit var cityField: MaterialAutoCompleteTextView
    private lateinit var dateField: TextView
    private lateinit var nightsField: EditText
    private lateinit var peoplesField: EditText

    private lateinit var priceNumberFrom: TextView
    private lateinit var priceNumberTo: TextView
    private lateinit var rangeSliderPrice: RangeSlider
    private lateinit var starsNumberFrom: TextView
    private lateinit var starsNumberTo: TextView
    private lateinit var rangeSliderStars: RangeSlider
    private lateinit var foodTypesRadioGroupFilters: RadioGroup
    private lateinit var foodSystemsRadioGroupFilters: RadioGroup
    private lateinit var infrastructureCheckBox1: MaterialCheckBox
    private lateinit var infrastructureCheckBox2: MaterialCheckBox
    private lateinit var infrastructureCheckBox3: MaterialCheckBox
    private lateinit var infrastructureCheckBox4: MaterialCheckBox
    private lateinit var infrastructureCheckBox5: MaterialCheckBox
    private lateinit var infrastructureCheckBox6: MaterialCheckBox
    private lateinit var infrastructureCheckBox7: MaterialCheckBox


    private lateinit var currentCurrency: String // текущая валюта

    private lateinit var calendar: Calendar // календарь для выбора даты вылета в меню выбора города
    private var minYear = 0 // минимальный год для выбора даты вылета
    private var minMonth = 0 // минимальный месяц для выбора даты вылета
    private var minDay = 0 // минимальный день для выбора даты вылета

    private var minimumNumberOfNights: Int = 1
    private var maximumNumberOfNights: Int = 30
    private var minimumNumberOfPeople: Int = 1
    private var maximumNumberOfPeople: Int = 10

    private var cities = arrayOf<String>() // массив с городами


    val presenter: ToursMainPresenter by moxyPresenter {
        toursSubComponent = App.instance.initUserSubComponent()

        ToursMainPresenter().apply {
            toursSubComponent?.inject(this)
        }
    }

    var adapter: ToursRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToursMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter.onRefreshAction()
        }


    }


    override fun initBottomNavigationMenu() {
        binding.bottomNavigation.menu.findItem(R.id.item_search).isChecked = true
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_search -> {
                    presenter.navigationSearchOnClick()
                    return@setOnItemSelectedListener true
                }

                R.id.item_hot_tours -> {
                    presenter.navigationHotToursOnClick()
                    return@setOnItemSelectedListener true
                }

                R.id.item_favourite -> {
                    presenter.navigationFavouriteOnClick()
                    return@setOnItemSelectedListener true
                }

                R.id.item_profile -> {
                    presenter.navigationProfileOnClick()
                    return@setOnItemSelectedListener true
                }

                else -> {
                    false
                }
            }

        }
    }

    override fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun setVisibilityNotFoundLayout(visibility: Boolean) {
        if (visibility) binding.notFoundLayout.visibility = View.VISIBLE
        else binding.notFoundLayout.visibility = View.GONE
    }

    override fun setVisibilityRecyclerView(visibility: Boolean) {
        if (visibility) binding.recyclerView.visibility = View.VISIBLE
        else binding.recyclerView.visibility = View.GONE
    }

    override fun stopRefreshing() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun getCitiesArrayFromResourses() {
        presenter.setCities(resources.getStringArray(R.array.cities))
    }

    override fun setCitiesList(cities: Array<String>) {
        this.cities = cities
    }

    override fun setMinimumNumberNights(minimumNumberOfNights: Int) {
        this.minimumNumberOfNights = minimumNumberOfNights
    }

    override fun setMaximumNumberNights(maximumNumberOfNights: Int) {
        this.maximumNumberOfNights = maximumNumberOfNights
    }

    override fun setMinimumNumberPeoples(minimumNumberOfPeoples: Int) {
        this.minimumNumberOfPeople = minimumNumberOfPeoples
    }

    override fun setMaximumNumberPeoples(maximumNumberOfPeoples: Int) {
        this.maximumNumberOfPeople = maximumNumberOfPeoples
    }

    override fun initFiltersButton() {
        binding.filtersButton.setOnClickListener {// кнопка "Фильтры"
            presenter.filtersButtonOnClick()
        }
    }

    override fun showFiltersDialog() {
        filtersDialog.show()
    }

    override fun initSortingButton() {
        binding.sortingButton.setOnClickListener {
            presenter.sortingButtonOnClick()
        }
    }


    override fun initCityAndNightsButton() {
        binding.cityAndNightsButton.root.setOnClickListener {
            presenter.cityAndNightsButtonOnClick()
        }
    }

    override fun showCityDialog() {
        cityDialog.show()
    }

    override fun initCurrentCurrency(currentCurrency: String) {
        this.currentCurrency = currentCurrency
    }

    override fun setTextSortingButton(text: String) {
        binding.sortingButton.text = text
    }

    override fun initSortingMenu(sortingStrings: List<String>) { // обработка меню сортировки
        val popupMenu = PopupMenu(requireContext(), binding.sortingButton)
        for (option in sortingStrings) {
            popupMenu.menu.add(option)
        }
        popupMenu.setOnMenuItemClickListener {// нажатие на один из элементов меню сортировки
            when (it.title) {
                sortingStrings[0] -> { // Рекомендуемое
                    presenter.sortingItemOnClick(it.title.toString())
                    true
                }

                sortingStrings[1] -> { // Сначала новое
                    presenter.sortingItemOnClick(it.title.toString())
                    true
                }

                sortingStrings[2] -> { // Дешевле
                    presenter.sortingItemOnClick(it.title.toString())
                    true
                }

                sortingStrings[3] -> { // Дороже
                    presenter.sortingItemOnClick(it.title.toString())
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    override fun initFiltersDialog() { // init меню фильтров


        val bottomSheet = layoutInflater.inflate(R.layout.filters_bottom_sheet_layout, null)
        filtersDialog = BottomSheetDialog(requireContext())
        filtersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filtersDialog.setContentView(bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet.parent as View)
        bottomSheetBehavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.dialog_filters_peek_height) // Устанавливаем высоту, на которую откроется BottomSheet

        initFiltersViews() // init вью из диалога с фильтрами
        initInfrastructureCheckboxes(filtersDialog) // init чекбоксов из фильтров

        rangeSliderPriceInit()
        rangeSliderStarsInit()
        cancelButtonFiltersInit()
        clearButtonFiltersInit()
        initOnCancelFiltersDialog()
    }

    override fun setValuesFiltersDialog(
        priceNumberFromValue: String,
        priceNumberToValue: String,
        starsNumberFromValue: String,
        starsNumberToValue: String,
        foodTypesRadioGroupCheckedId: String,
        foodSystemsRadioGroupCheckedId: String,
        infrastructureCheckBox1IsChecked: Boolean,
        infrastructureCheckBox2IsChecked: Boolean,
        infrastructureCheckBox3IsChecked: Boolean,
        infrastructureCheckBox4IsChecked: Boolean,
        infrastructureCheckBox5IsChecked: Boolean,
        infrastructureCheckBox6IsChecked: Boolean,
        infrastructureCheckBox7IsChecked: Boolean
    ) {
        priceNumberFrom.text = priceNumberFromValue
        priceNumberTo.text = priceNumberToValue

        starsNumberFrom.text = starsNumberFromValue
        starsNumberTo.text = starsNumberToValue

        rangeSliderPrice.setValues(priceNumberFromValue.toFloat(), priceNumberToValue.toFloat())
        rangeSliderStars.setValues(starsNumberFromValue.toFloat(), starsNumberToValue.toFloat())

        when (foodTypesRadioGroupCheckedId) {
            "food_types_radiobutton_1" -> filtersDialog.findViewById<MaterialRadioButton>(
                R.id.food_types_radiobutton_1
            )?.id

            "food_types_radiobutton_2" -> filtersDialog.findViewById<MaterialRadioButton>(
                R.id.food_types_radiobutton_2
            )?.id

            "food_types_radiobutton_3" -> filtersDialog.findViewById<MaterialRadioButton>(
                R.id.food_types_radiobutton_3
            )?.id

            else -> foodTypesRadioGroupCheckedId.toInt()
        }?.let {
            foodTypesRadioGroupFilters.check(
                it as Int
            )
        }

        when (foodSystemsRadioGroupCheckedId) {
            "food_systems_radiobutton_1" -> filtersDialog.findViewById<MaterialRadioButton>(
                R.id.food_radiobutton_1
            )?.id

            "food_systems_radiobutton_2" -> filtersDialog.findViewById<MaterialRadioButton>(
                R.id.food_radiobutton_2
            )?.id

            "food_systems_radiobutton_3" -> filtersDialog.findViewById<MaterialRadioButton>(
                R.id.food_radiobutton_3
            )?.id

            "food_systems_radiobutton_4" -> filtersDialog.findViewById<MaterialRadioButton>(
                R.id.food_radiobutton_4
            )?.id

            "food_systems_radiobutton_5" -> filtersDialog.findViewById<MaterialRadioButton>(
                R.id.food_radiobutton_5
            )?.id

            "food_systems_radiobutton_6" -> filtersDialog.findViewById<MaterialRadioButton>(
                R.id.food_radiobutton_6
            )?.id

            else -> foodSystemsRadioGroupCheckedId.toInt()
        }?.let {
            foodSystemsRadioGroupFilters.check(
                it
            )
        }

        infrastructureCheckBox1.isChecked = infrastructureCheckBox1IsChecked
        infrastructureCheckBox2.isChecked = infrastructureCheckBox2IsChecked
        infrastructureCheckBox3.isChecked = infrastructureCheckBox3IsChecked
        infrastructureCheckBox4.isChecked = infrastructureCheckBox4IsChecked
        infrastructureCheckBox5.isChecked = infrastructureCheckBox5IsChecked
        infrastructureCheckBox6.isChecked = infrastructureCheckBox6IsChecked
        infrastructureCheckBox7.isChecked = infrastructureCheckBox7IsChecked
    }

    private fun getValuesFiltersDialog(
        priceNumberFromKey: String,
        priceNumberToKey: String,
        starsNumberFromKey: String,
        starsNumberToKey: String,
        foodTypesRadioGroupKey: String,
        foodSystemsRadioGroupKey: String,
        infrastructureCheckBox1Key: String,
        infrastructureCheckBox2Key: String,
        infrastructureCheckBox3Key: String,
        infrastructureCheckBox4Key: String,
        infrastructureCheckBox5Key: String,
        infrastructureCheckBox6Key: String,
        infrastructureCheckBox7Key: String

    ): MutableMap<String, String> {
        val savedValuesFiltersDialog = mutableMapOf<String, String>() // сохраненные значения для основных view в меню фильтров
        savedValuesFiltersDialog.put(priceNumberFromKey, rangeSliderPrice.values[0].toString())
        savedValuesFiltersDialog.put(priceNumberToKey, rangeSliderPrice.values[1].toString())

        savedValuesFiltersDialog.put(starsNumberFromKey, rangeSliderStars.values[0].toInt().toString())
        savedValuesFiltersDialog.put(starsNumberToKey, rangeSliderStars.values[1].toInt().toString())

        savedValuesFiltersDialog.put(foodTypesRadioGroupKey, foodTypesRadioGroupFilters.checkedRadioButtonId.toString())
        savedValuesFiltersDialog.put(foodSystemsRadioGroupKey, foodSystemsRadioGroupFilters.checkedRadioButtonId.toString())

        savedValuesFiltersDialog.put(infrastructureCheckBox1Key, infrastructureCheckBox1.isChecked.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox2Key, infrastructureCheckBox2.isChecked.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox3Key, infrastructureCheckBox3.isChecked.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox4Key, infrastructureCheckBox4.isChecked.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox5Key, infrastructureCheckBox5.isChecked.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox6Key, infrastructureCheckBox6.isChecked.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox7Key, infrastructureCheckBox7.isChecked.toString())

        return savedValuesFiltersDialog
    }

    fun rangeSliderPriceInit() {
        rangeSliderPrice.addOnChangeListener { slider, value, fromUser -> // изменения в слайдере с ценой
            presenter.priceSliderOnChange(slider.values[0].toInt().toString(), slider.values[1].toInt().toString())
        }
    }

    fun rangeSliderStarsInit() {
        rangeSliderStars.addOnChangeListener { slider, value, fromUser -> // изменения в слайдере с ценой
            presenter.priceStarsOnChange(slider.values[0].toInt().toString(), slider.values[1].toInt().toString())
        }
    }

    fun cancelButtonFiltersInit() {
        val cancelButtonFilters = filtersDialog.findViewById<TextView>(R.id.filters_layout_cancel_button)
        cancelButtonFilters?.setOnClickListener {
            presenter.cancelButtonFiltersOnClick()
        }
    }

    fun clearButtonFiltersInit() {
        val clearButtonFilters = filtersDialog.findViewById<TextView>(R.id.filters_layout_clear_button)
        clearButtonFilters?.setOnClickListener {
            presenter.clearButtonFiltersOnClick()
        }
    }

    private fun initOnCancelFiltersDialog() {
        val priceNumberFromKey = "priceNumberFrom" // ключ для сохранения "цены от" в map
        val priceNumberToKey = "priceNumberTo" // ключ для сохранения "цены до" в map
        val starsNumberFromKey = "starsNumberFrom" // ключ для сохранения "колво звезд от" в map
        val starsNumberToKey = "starsNumberTo" // ключ для сохранения  "колво звезд до" в map
        val foodTypesRadioGroupKey = "foodTypesRadioGroup" // ключ для сохранения checked элемента в radiogroup типов питания
        val foodSystemsRadioGroupKey = "foodSystemsRadioGroup" // ключ для сохранения checked элемента в radiogroup систем питания
        val infrastructureCheckBox1Key = "infrastructureCheckBox1" // ключ для сохранения состояния чекбокса 1
        val infrastructureCheckBox2Key = "infrastructureCheckBox2" // ключ для сохранения состояния чекбокса 2
        val infrastructureCheckBox3Key = "infrastructureCheckBox3" // ключ для сохранения состояния чекбокса 3
        val infrastructureCheckBox4Key = "infrastructureCheckBox4" // ключ для сохранения состояния чекбокса 4
        val infrastructureCheckBox5Key = "infrastructureCheckBox5" // ключ для сохранения состояния чекбокса 5
        val infrastructureCheckBox6Key = "infrastructureCheckBox6" // ключ для сохранения состояния чекбокса 6
        val infrastructureCheckBox7Key = "infrastructureCheckBox7" // ключ для сохранения состояния чекбокса 7

        filtersDialog.setOnCancelListener {
            presenter.filtersDialogOnCancel(
                getValuesFiltersDialog(
                    priceNumberFromKey,
                    priceNumberToKey,
                    starsNumberFromKey,
                    starsNumberToKey,
                    foodTypesRadioGroupKey,
                    foodSystemsRadioGroupKey,
                    infrastructureCheckBox1Key,
                    infrastructureCheckBox2Key,
                    infrastructureCheckBox3Key,
                    infrastructureCheckBox4Key,
                    infrastructureCheckBox5Key,
                    infrastructureCheckBox6Key,
                    infrastructureCheckBox7Key
                )
            )
        }
    }

    override fun cancelFiltersDialog() {
        filtersDialog.cancel()
    }

    override fun setValueToPriceNumberFrom(text: String) {
        priceNumberFrom.text = text
    }

    override fun setValueToPriceNumberTo(text: String) {
        priceNumberTo.text = text
    }

    override fun setValueToStarsNumberFrom(text: String) {
        starsNumberFrom.text = text
    }

    override fun setValueToStarsNumberTo(text: String) {
        starsNumberTo.text = text
    }

    private fun initFiltersViews() { // init вью из диалога с фильтрами
        filtersDialog.findViewById<TextView>(R.id.price_number_from).let {
            if (it != null) {
                priceNumberFrom = it
            }
        } // textView с ценой "от"
        filtersDialog.findViewById<TextView>(R.id.price_number_to).let {
            if (it != null) {
                priceNumberTo = it
            }
        } // textView с ценой "до"
        filtersDialog.findViewById<RangeSlider>(R.id.price_slider).let {
            if (it != null) {
                rangeSliderPrice = it
            }
        } // слайдер с ценой

        filtersDialog.findViewById<TextView>(R.id.stars_number_from).let {
            if (it != null) {
                starsNumberFrom = it
            }
        } // textView с ценой "от"
        filtersDialog.findViewById<TextView>(R.id.stars_number_to).let {
            if (it != null) {
                starsNumberTo = it
            }
        } // textView с ценой "до"
        filtersDialog.findViewById<RangeSlider>(R.id.stars_slider).let {
            if (it != null) {
                rangeSliderStars = it
            }
        } // слайдер со звездами
        filtersDialog.findViewById<RadioGroup>(R.id.food_types_radiogroup).let {
            if (it != null) {
                foodTypesRadioGroupFilters = it
            }
        }
        filtersDialog.findViewById<RadioGroup>(R.id.food_systems_radiogroup).let {
            if (it != null) {
                foodSystemsRadioGroupFilters = it
            }
        }
        filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_1).let {
            if (it != null) {
                infrastructureCheckBox1 = it
            }
        }
        filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_2).let {
            if (it != null) {
                infrastructureCheckBox2 = it
            }
        }
        filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_3).let {
            if (it != null) {
                infrastructureCheckBox3 = it
            }
        }
        filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_4).let {
            if (it != null) {
                infrastructureCheckBox4 = it
            }
        }
        filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_5).let {
            if (it != null) {
                infrastructureCheckBox5 = it
            }
        }
        filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_6).let {
            if (it != null) {
                infrastructureCheckBox6 = it
            }
        }
        filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_7).let {
            if (it != null) {
                infrastructureCheckBox7 = it
            }
        }

    }


    override fun initCityDialog() { // инициализация диалога с выбором города и даты
        citySheet = layoutInflater.inflate(R.layout.city_sheet_layout, null)
        cityDialog = Dialog(requireContext(), R.style.CityDateDialogStyle)
        cityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        cityDialog.setContentView(citySheet)
        val window: Window = cityDialog.window!!
        val param: WindowManager.LayoutParams = window.attributes
        param.width = WindowManager.LayoutParams.MATCH_PARENT
//        param.height = resources.getDimensionPixelSize(R.dimen.dialog_city_peek_height)
        param.height = WindowManager.LayoutParams.WRAP_CONTENT
        param.gravity = Gravity.TOP
        window.attributes = param

        window.setWindowAnimations(R.style.DialogAnimation)

        initViewsForSaving() // init основных view с данными, которые нужно сохранять

        initCancelButtonCityDialog() // кнопка "Отмена"

        initAutoCompleteCityDestinationCityDialog() // выбор города
        initButtonClearAutoCompleteCityDialog() // очистить поле выбора города

        initDatePickerCityDialog() // выбор даты

        initNightsCounter(cityDialog) // количество ночей
        initPeoplesCounter(cityDialog) // количество людей

//        initFindButton(cityDialog) // кнопка "Найти туры"/"Найти отели"

//        initSavedValueCityDialogMap() // важно чтобы получение сохраненных значений было после initDatePickerCityDialog()

        cityDialog.setOnCancelListener { // listener на закрытие диалога с фильтрами
//            getSavedValuesForCityDialog() // получение сохраненных значений, если была нажата кнопка "Отмена", вернутся старые значения, в другом случае ничего не изменится

            cityDialog.dismiss() // диалог скрывается, но не удаляется
        }
    }

    private fun initViewsForSaving() { // init views, которые используются при сохранении данных
        cityField = cityDialog.findViewById(R.id.city_menu_autocomplete_tv)
        dateField = cityDialog.findViewById(R.id.city_menu_date_picker_tv)
        nightsField = cityDialog.findViewById(R.id.city_menu_nights_value)
        peoplesField = cityDialog.findViewById(R.id.city_menu_peoples_value)
    }

    override fun dismissCityDialog() {
        cityDialog.dismiss()
    }

    override fun initFindButtonCityDialog() { // init кнопки "Найти туры"/"Найти отели" в диалоге выбора города
        findButton = cityDialog.findViewById(R.id.city_menu_find_button)

        findButton.setOnClickListener {
            if (isSelectedCitiesValid()) { // проверка на валидность полей с городами
                saveValuesForCityDialog()
                presenter.findButtonCityDialogOnClick()

//                saveValuesForCityDialog()
//                cityDialog.dismiss()
//                updateCityButton()
            }
        }

    }

//    private fun initSavedValueCityDialogMap() { // важно чтобы получение сохраненных значений было после  initDatePickerCityDialog()
//        // init map с дефолтными значениями
//        savedValuesCityDialog.put(cityNameKeyCityDialog, "")
//        savedValuesCityDialog.put(dateKeyCityDialog, getDateOfDeparture())
//        savedValuesCityDialog.put(cityDepartureNameKeyCityDialog, "")
//        savedValuesCityDialog.put(nightsKeyCityDialog, resources.getString(R.string.nights_value_city_layout))
//        savedValuesCityDialog.put(peoplesKeyCityDialog, resources.getString(R.string.peoples_value_city_layout))
//    }

    private fun saveValuesForCityDialog() { // сохранение значений из основных view из cityDialog в map
        val savedValuesCityDialog = mutableMapOf<String, String>()

        val cityNameKeyCityDialog = "cityName" // ключ для сохранения города в map
        val dateKeyCityDialog = "date" // ключ для сохранения даты вылета в map
        val nightsKeyCityDialog = "nights" // ключ для сохранения количества ночей в map
        val peoplesKeyCityDialog = "peoples" // ключ для сохранения количества людей в map

        savedValuesCityDialog.put(cityNameKeyCityDialog, cityField.text.toString())
        savedValuesCityDialog.put(dateKeyCityDialog, dateField.text.toString())
        savedValuesCityDialog.put(nightsKeyCityDialog, nightsField.text.toString())
        savedValuesCityDialog.put(peoplesKeyCityDialog, peoplesField.text.toString())

        presenter.saveValuesCityDialog(savedValuesCityDialog)
    }

    override fun setValuesCityDialog(
        cityName: String,
        date: String,
        nightsNumber: String,
        peoplesNumber: String
    ) { // получение сохраненных значений для основных view в диалоге выбора города

        cityField.setText(cityName)
        dateField.setText(date)
        nightsField.setText(nightsNumber)
        peoplesField.setText(peoplesNumber)

    }


    private fun initCancelButtonCityDialog() { // init кнопки "Отмена" в диалоге выбора города
        cityDialog.findViewById<Button>(R.id.city_menu_cancel_button).setOnClickListener {// кнопка "Отмена"
            presenter.cancelButtonCityDialogOnClick()
        }
    }

    override fun cancelCityDialog() {
        cityDialog.cancel()
    }

    private fun initNightsCounter(cityDialog: Dialog) { // инит количества ночей в диалоге выбора города
        val counterTextView = cityDialog.findViewById<EditText>(R.id.city_menu_nights_value)
        val plusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_nights_plus_button)
        val minusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_nights_minus_button)

        counterTextView.addTextChangedListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString().isNotEmpty()) {
                if (counterTextView.text.toString().toInt() > maximumNumberOfNights) {
                    counterTextView.setText("$maximumNumberOfNights")
                    showSnackBarAboutValue("Максимальное количество ночей - $maximumNumberOfNights")
                }
                if (counterTextView.text.toString().toInt() < minimumNumberOfNights) {
                    counterTextView.setText("$minimumNumberOfNights")
                    showSnackBarAboutValue("Минимальное количество ночей - $minimumNumberOfNights")
                }
                counterTextView.setSelection(counterTextView.text.length)
            }
        }
        plusButton.setOnClickListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString()
                    .isNotEmpty()
            ) counterTextView.setText((counterTextView.text.toString().toInt() + 1).toString())
            else counterTextView.setText("$minimumNumberOfNights")
            counterTextView.setSelection(counterTextView.text.length)
        }
        minusButton.setOnClickListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString()
                    .isNotEmpty()
            ) counterTextView.setText((counterTextView.text.toString().toInt() - 1).toString())
            else counterTextView.setText("$minimumNumberOfNights")
            counterTextView.setSelection(counterTextView.text.length)
        }
    }

    private fun showSnackBarAboutValue(text: String) { // показать снекбар, что количество неверное, сделано для выбора количества людей и ночей в диалоге выбора города
        Snackbar.make(citySheet, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun initPeoplesCounter(cityDialog: Dialog) { // инит количества людей в диалоге выбора города
        val counterTextView = cityDialog.findViewById<EditText>(R.id.city_menu_peoples_value)
        val plusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_peoples_plus_button)
        val minusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_peoples_minus_button)

        counterTextView.addTextChangedListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString().isNotEmpty()) {
                if (counterTextView.text.toString().toInt() > maximumNumberOfPeople) {
                    counterTextView.setText("$maximumNumberOfPeople")
                    showSnackBarAboutValue("Максимальное количество человек - $maximumNumberOfPeople")
                }
                if (counterTextView.text.toString().toInt() < minimumNumberOfPeople) {
                    counterTextView.setText("$minimumNumberOfPeople")
                    showSnackBarAboutValue("Минимальное количество человек - $minimumNumberOfPeople")
                }
                counterTextView.setSelection(counterTextView.text.length)
            }
        }
        plusButton.setOnClickListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString()
                    .isNotEmpty()
            ) counterTextView.setText((counterTextView.text.toString().toInt() + 1).toString())
            else counterTextView.setText("$minimumNumberOfPeople")
            counterTextView.setSelection(counterTextView.text.length)
        }
        minusButton.setOnClickListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString()
                    .isNotEmpty()
            ) counterTextView.setText((counterTextView.text.toString().toInt() - 1).toString())
            else counterTextView.setText("$minimumNumberOfPeople")
            counterTextView.setSelection(counterTextView.text.length)
        }
    }

    private fun initDatePickerCityDialog() { // выбор даты вылета диалог
        lateinit var startDatePicker: DatePickerDialog
        var startDate: Date
        val textViewButtonDate = cityDialog.findViewById<TextView>(R.id.city_menu_date_picker_tv)
        textViewButtonDate.text = presenter.getDateOfDeparture() // дата вылета = завтра

//        textViewButtonDate.text = formatDate(Date().) // сегодняшняя дата

        // Создаем DatePickerDialog для выбора даты вылета
        startDatePicker = createDatePicker { selectedDate ->
            startDate = selectedDate
            val startDateStr = presenter.formatDate(startDate)
            textViewButtonDate.text = startDateStr
        }
        textViewButtonDate.setOnClickListener {
            startDatePicker.show()
        }
    }

//    private fun getDateOfDeparture() = formatDate(calendar.time)

    private fun createDatePicker(onDateSelected: (Date) -> Unit): DatePickerDialog { // создание окна выбора даты для кнопки выбора числа вылета

        val listener = DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            onDateSelected(calendar.time)
        }
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            listener,
            minYear, minMonth, minDay
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis // минимальная дата = завтра

        return datePickerDialog
    }

//    private fun formatDate(date: Date?): String { // формат даты для выбора числа вылета
//        val format = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
//        return format.format(date)
//    }


    override fun updateCityButton(cityName: String, nightsNumber: String) { // обновление текста в кнопке выбора города на главном лэйауте

        val textForCityButtonDaysNumber =
            makeTextForCityButtonDaysNumber(presenter.getDateOfDeparture(), nightsNumber)
        binding.cityAndNightsButton.daysNumberTv.text = textForCityButtonDaysNumber

        if (cityName.isBlank()) binding.cityAndNightsButton.cityNameTv.text =
            resources.getString(R.string.city_and_nights_button_default_text)
        else binding.cityAndNightsButton.cityNameTv.text = cityName
    }

    private fun makeTextForCityButtonDaysNumber(
        date: String,
        numberOfNights: String
    ): String { // форматирует данные для текста в кнопке города в mainLayout
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val parsedDate = dateFormat.parse(date)

        val modifiedFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        val modifiedDate = modifiedFormat.format(parsedDate)

        return "$modifiedDate, $numberOfNights ночей"

    }

    override fun initMinDate(calendar: Calendar, minYear: Int, minMonth: Int, minDay: Int) { // установка минимальной даты вылета
        this.calendar = calendar
        this.minYear = minYear
        this.minMonth = minMonth
        this.minDay = minDay

    }

    private fun initButtonClearAutoCompleteCityDialog() { // кнопка очистки рядом с полем выбора города, куда планируется тур
        val clearButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_clear_button)
        clearButton.setOnClickListener {
            cityDialog.findViewById<MaterialAutoCompleteTextView>(R.id.city_menu_autocomplete_tv).text.clear()
        }
    }


    private fun initAutoCompleteCityDestinationCityDialog() { // инит поля с выбором города, куда планируется тур

        val autoCompleteTextView = cityDialog.findViewById<MaterialAutoCompleteTextView>(R.id.city_menu_autocomplete_tv) // поле с выбором города
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        autoCompleteTextView.setAdapter(adapter)

    }


    private fun isSelectedCitiesValid(): Boolean { // проверка на валидность полея с выбором города с городами

        if (cityField.text.toString().isBlank()
        ) return true // если поле города назначения пустое, то пропускаем, будет "Россия"

        for (item in cities) { // проверка, есть ли в списке городов город назначения
            if (item.toString() == cityField.text.toString()) return true
        }

        showSnackBarAboutValue("Некорректное название города, пожалуйста, выберите из выпадающего списка")
        return false
    }


    private fun initInfrastructureCheckboxes(filtersDialog: BottomSheetDialog) { // init checkboxes + textviews в фильтрах
        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_1)
            ?.setOnClickListener { // onClickListener на TextView рядом с чекбоксом
                with(infrastructureCheckBox1) {
                    this.isChecked = !this.isChecked // при нажатии на TextView, меняется состояние чекбокса рядом
                }
            }
        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_2)?.setOnClickListener {
            with(infrastructureCheckBox2) {
                this.isChecked = !this.isChecked
            }
        }
        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_3)?.setOnClickListener {
            with(infrastructureCheckBox3) {
                this.isChecked = !this.isChecked
            }
        }
        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_4)?.setOnClickListener {
            with(infrastructureCheckBox4) {
                this.isChecked = !this.isChecked
            }
        }
        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_5)?.setOnClickListener {
            with(infrastructureCheckBox5) {
                this.isChecked = !this.isChecked
            }
        }
        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_6)?.setOnClickListener {
            with(infrastructureCheckBox6) {
                this.isChecked = !this.isChecked
            }
        }
        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_7)?.setOnClickListener {
            with(infrastructureCheckBox7) {
                this.isChecked = !this.isChecked
            }
        }
    }



    override fun updateImage(position: Int) {
        val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(position)
        if (viewHolder != null && viewHolder is ToursRVAdapter.ViewHolder) {
            viewHolder.favouriteImageView.setImageResource(R.drawable.baseline_favorite_24)
        }
    }


    override fun init() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ToursRVAdapter(presenter.toursListPresenter)
        binding.recyclerView.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun release() {
        toursSubComponent = null
        App.instance.releaseUserSubComponent()
    }

    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ToursMainFragment()
    }


}