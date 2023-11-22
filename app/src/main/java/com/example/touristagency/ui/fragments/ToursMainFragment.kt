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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.ToursSubComponent
import com.example.touristagency.databinding.FragmentToursMainBinding
import com.example.touristagency.mvp.presenter.ToursMainPresenter
import com.example.touristagency.mvp.view.SlideShowAdapter
import com.example.touristagency.mvp.view.ToursView
import com.example.touristagency.ui.activity.BackButtonListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
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

    private var isToursButtonActive = true // флаг, активна ли кнопка "Туры"

    private val sortingStrings = listOf("Рекомендуемое", "По рейтингу", "Дешевле", "Дороже") // способы сортировки для меню сортировки

    private lateinit var cityDialog: Dialog // диалог с выбором города и даты
    private lateinit var findButton: MaterialButton // кнопка "Найти туры"/"Найти  отели" в cityDialog
    private lateinit var filtersDialog: BottomSheetDialog // диалог с фильтрами
    private lateinit var citySheet: View // layout для диалога с выбором города и даты (cityDialog)

    private lateinit var cityField: MaterialAutoCompleteTextView
    private lateinit var dateField: TextView
    private lateinit var cityDepartureField: MaterialAutoCompleteTextView
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

    private val savedValuesCityDialog = mutableMapOf<String, String>() // сохраненные значения для основных view в cityDialog
    private val cityNameKeyCityDialog = "cityName" // ключ для сохранения города в map
    private val dateKeyCityDialog = "date" // ключ для сохранения даты вылета в map
    private val cityDepartureNameKeyCityDialog = "cityDeparture" // ключ для сохранения города вылета в map
    private val nightsKeyCityDialog = "nights" // ключ для сохранения количества ночей в map
    private val peoplesKeyCityDialog = "peoples" // ключ для сохранения количества людей в map


    private val savedValuesFiltersDialog = mutableMapOf<String, String>() // сохраненные значения для основных view в меню фильтров
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

    private var cities = arrayOf<String>() // массив с городами


    val presenter: ToursMainPresenter by moxyPresenter {
        toursSubComponent = App.instance.initUserSubComponent()

        ToursMainPresenter().apply {
            toursSubComponent?.inject(this)
        }
    }

//    var adapter: UsersRVAdapter? = null

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

        cities = resources.getStringArray(R.array.cities)

        currentCurrency = resources.getString(R.string.current_currency)
        initMinDate()


        initCityDialog()
        updateCityButton()
        binding.cityAndNightsButton.root.setOnClickListener {
            cityDialog.show()
        }

        binding.toursButton.setOnClickListener { // кнопка "Туры"
            switchStateButtonsToursAndHotels(true)
        }

        binding.hotelsButton.setOnClickListener { // кнопка "Отели"
            switchStateButtonsToursAndHotels(false)
        }


        binding.sortingButton.text = sortingStrings[0] // присвоение дефолтного способа сортировки
        binding.sortingButton.setOnClickListener { initSortingMenu() } // кнопка "Сортировка"

        initFiltersDialog()
        binding.filtersButton.setOnClickListener {// кнопка "Фильтры"
            filtersDialog.show()
        }


        testInitFirstRecyclerItem()
        testInitSecondRecyclerItem()


    }

    private fun updateCityButton() { // обновление текста в кнопке выбора города на главном лэйауте

        val textForCityButtonDaysNumber =
            savedValuesCityDialog[nightsKeyCityDialog]?.let { makeTextForCityButtonDaysNumber(getDateOfDeparture(), it) }
        binding.cityAndNightsButton.daysNumberTv.text = textForCityButtonDaysNumber

        if (savedValuesCityDialog[cityNameKeyCityDialog].isNullOrBlank()) binding.cityAndNightsButton.cityNameTv.text =
            resources.getString(R.string.city_and_nights_button_default_text)
        else binding.cityAndNightsButton.cityNameTv.text = savedValuesCityDialog[cityNameKeyCityDialog]
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

    private fun testInitFirstRecyclerItem() { // прокручивающиеся картинки в recyclerViewItem // todo переработать


        // Создаем список изображений
        val images = listOf(
            R.drawable.alean_1,
            R.drawable.alean_2,
            R.drawable.alean_3,
            R.drawable.alean_4,
            R.drawable.alean_5,
            R.drawable.alean_6,
            R.drawable.alean_7,
            R.drawable.alean_8,
            R.drawable.alean_9,
            R.drawable.alean_10
        )

// Создаем экземпляр PagerAdapter и устанавливаем его во ViewPager2
        val adapter = SlideShowAdapter(images)
        binding.testItem.recyclerItemTourImageLayoutViewpager2.adapter = adapter


//        binding.testItem.recyclerItemTourFavouriteButton.setOnClickListener {
//            binding.testItem2.recyclerItemTourFavouriteButton.setImageResource(R.drawable.baseline_favorite_24)
//        }

    }

    private fun testInitSecondRecyclerItem() {

        binding.testItem2.recyclerItemTourHotelName.text = "Санаторий \"Надежда\""
        binding.testItem2.recyclerItemTourHotelLocation.text = "Анапа, Россия"
        binding.testItem2.recyclerItemTourHotelRating.text = "9.0"

        binding.testItem2.recyclerItemTourLineImage.setImageResource(R.drawable.second_24)
        binding.testItem2.recyclerItemTourAirportTextView.text = "в 1 км"
        binding.testItem2.recyclerItemTourBeachTextView.text = "650 м"

        binding.testItem2.recyclerItemTourPriceTextView.text = "40 627 $currentCurrency"

        binding.testItem2.recyclerItemTourParkingTextView.visibility = View.GONE
        binding.testItem2.recyclerItemTourParkingImage.visibility = View.GONE

        binding.testItem2.recyclerItemTourFavouriteButton.setOnClickListener {
            // todo прокинуть метод в presenter

        }


        // Создаем список изображений
        val images = listOf(
            R.drawable.nadezhda_1,
            R.drawable.nadezhda_2,
            R.drawable.nadezhda_3,
            R.drawable.nadezhda_4,
            R.drawable.nadezhda_5,
            R.drawable.nadezhda_6,
            R.drawable.nadezhda_7,
            R.drawable.nadezhda_8,
            R.drawable.nadezhda_9,
            R.drawable.nadezhda_10
        )

// Создаем экземпляр PagerAdapter и устанавливаем его во ViewPager2
        val adapter = SlideShowAdapter(images)
        binding.testItem2.recyclerItemTourImageLayoutViewpager2.adapter = adapter

    }

    private fun initFiltersDialog() { // init меню фильтров


        val bottomSheet = layoutInflater.inflate(R.layout.filters_bottom_sheet_layout, null)
        filtersDialog = BottomSheetDialog(requireContext())
        filtersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filtersDialog.setContentView(bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet.parent as View)
        bottomSheetBehavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.dialog_filters_peek_height) // Устанавливаем высоту, на которую откроется BottomSheet

        initFiltersViews() // init вью из диалога с фильтрами
        initInfrastructureCheckboxes(filtersDialog) // init чекбоксов из фильтров

        getSavedFiltersValues() // получение сохраненных значений

        rangeSliderPrice.addOnChangeListener { slider, value, fromUser -> // изменения в слайдере с ценой
            priceNumberFrom.text = slider.values[0].toInt().toString() + currentCurrency // изменение подписи цены под слайдером
            priceNumberTo.text = slider.values[1].toInt().toString() + currentCurrency // изменение подписи цены под слайдером
        }

        rangeSliderStars.addOnChangeListener { slider, value, fromUser -> // изменения в слайдере со звездами
            starsNumberFrom.text = slider.values[0].toInt().toString()  // изменение подписи звезд под слайдером
            starsNumberTo.text = slider.values[1].toInt().toString() // изменение подписи звезд под слайдером
        }


        val cancelButtonFilters = filtersDialog.findViewById<TextView>(R.id.filters_layout_cancel_button)
        cancelButtonFilters?.setOnClickListener {
            getSavedFiltersValues() // присвоить вью старые значения
            filtersDialog.cancel()
        }

        val clearButtonFilters = filtersDialog.findViewById<TextView>(R.id.filters_layout_clear_button)
        clearButtonFilters?.setOnClickListener {
            getInitialValuesFilters()
            getSavedFiltersValues()
        }

        filtersDialog.setOnCancelListener { // listener на закрытие диалога с фильтрами
            saveValuesForFiltersDialog()
        }


    }

    private fun saveValuesForFiltersDialog() { // сохранить значения из диалога с фильтрами
        savedValuesFiltersDialog.put(priceNumberFromKey, rangeSliderPrice.values[0].toInt().toString())
        savedValuesFiltersDialog.put(priceNumberToKey, rangeSliderPrice.values[1].toInt().toString())

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
    }

    private fun getInitialValuesFilters() { // init map с дефолтными значениями
        savedValuesFiltersDialog.put(priceNumberFromKey, resources.getString(R.string.price_number_from_default))
        savedValuesFiltersDialog.put(priceNumberToKey, resources.getString(R.string.price_number_to_default))

        savedValuesFiltersDialog.put(starsNumberFromKey, resources.getString(R.string.stars_number_from_default))
        savedValuesFiltersDialog.put(starsNumberToKey, resources.getString(R.string.stars_number_to_default))

        savedValuesFiltersDialog.put(
            foodTypesRadioGroupKey,
            filtersDialog.findViewById<RadioButton>(R.id.food_types_radiobutton_1)?.id.toString()
        )

        savedValuesFiltersDialog.put(
            foodSystemsRadioGroupKey,
            filtersDialog.findViewById<RadioButton>(R.id.food_radiobutton_1)?.id.toString()
        )

        savedValuesFiltersDialog.put(infrastructureCheckBox1Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox2Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox3Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox4Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox5Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox6Key, false.toString())
        savedValuesFiltersDialog.put(infrastructureCheckBox7Key, false.toString())

    }

    private fun getSavedFiltersValues() { // присвоить сохраненные значения
        if (savedValuesFiltersDialog.isEmpty()) {
            getInitialValuesFilters()
        }

        // присвоение сохраненных значений - вью
        priceNumberFrom.text = savedValuesFiltersDialog[priceNumberFromKey] + currentCurrency
        priceNumberTo.text = savedValuesFiltersDialog[priceNumberToKey] + currentCurrency

        starsNumberFrom.text = savedValuesFiltersDialog[starsNumberFromKey]
        starsNumberTo.text = savedValuesFiltersDialog[starsNumberToKey]

        rangeSliderPrice.setValues(savedValuesFiltersDialog[priceNumberFromKey]?.toFloat(), savedValuesFiltersDialog[priceNumberToKey]?.toFloat())
        rangeSliderStars.setValues(savedValuesFiltersDialog[starsNumberFromKey]?.toFloat(), savedValuesFiltersDialog[starsNumberToKey]?.toFloat())

        savedValuesFiltersDialog[foodTypesRadioGroupKey]?.toInt()?.let { foodTypesRadioGroupFilters.check(it) }
        savedValuesFiltersDialog[foodSystemsRadioGroupKey]?.toInt()?.let { foodSystemsRadioGroupFilters.check(it) }

        infrastructureCheckBox1.isChecked = savedValuesFiltersDialog[infrastructureCheckBox1Key].toBoolean()
        infrastructureCheckBox2.isChecked = savedValuesFiltersDialog[infrastructureCheckBox2Key].toBoolean()
        infrastructureCheckBox3.isChecked = savedValuesFiltersDialog[infrastructureCheckBox3Key].toBoolean()
        infrastructureCheckBox4.isChecked = savedValuesFiltersDialog[infrastructureCheckBox4Key].toBoolean()
        infrastructureCheckBox5.isChecked = savedValuesFiltersDialog[infrastructureCheckBox5Key].toBoolean()
        infrastructureCheckBox6.isChecked = savedValuesFiltersDialog[infrastructureCheckBox6Key].toBoolean()
        infrastructureCheckBox7.isChecked = savedValuesFiltersDialog[infrastructureCheckBox7Key].toBoolean()


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


    private fun initCityDialog() { // инициализация диалога с выбором города и даты
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

        initCancelButtonCityDialog(cityDialog) // кнопка "Отмена"

        initTourAndHotelsButtonsCityDialog(cityDialog) // кнопки "Туры", "Отели"

        initAutoCompleteCityDialog(cityDialog) // выбор города
        initButtonClearAutoCompleteCityDialog(cityDialog) // очистить поле выбора города

        initAutoCompleteCityDestinationCityDialog(cityDialog) // выбор города откуда вылет
        initButtonClearAutoCompleteDestinationCityDialog(cityDialog) // очистить поле выбора города откуда вылет

        initDatePickerCityDialog(cityDialog) // выбор даты

        initNightsCounter(cityDialog) // количество ночей
        initPeoplesCounter(cityDialog) // количество людей

        initFindButton(cityDialog) // кнопка "Найти туры"/"Найти отели"

        initSavedValueCityDialogMap() // важно чтобы получение сохраненных значений было после initDatePickerCityDialog()

        cityDialog.setOnCancelListener { // listener на закрытие диалога с фильтрами
            getSavedValuesForCityDialog() // получение сохраненных значений, если была нажата кнопка "Отмена", вернутся старые значения, в другом случае ничего не изменится

            cityDialog.dismiss() // диалог скрывается, но не удаляется
        }
    }

    private fun initViewsForSaving() { // init views, которые используются при сохранении данных
        cityField = cityDialog.findViewById(R.id.city_menu_autocomplete_tv)
        dateField = cityDialog.findViewById(R.id.city_menu_date_picker_tv)
        cityDepartureField = cityDialog.findViewById(R.id.city_menu_autocomplete_tv_city_from)
        nightsField = cityDialog.findViewById(R.id.city_menu_nights_value)
        peoplesField = cityDialog.findViewById(R.id.city_menu_peoples_value)
    }

    private fun initFindButton(cityDialog: Dialog) { // init кнопки "Найти туры"/"Найти отели" в диалоге выбора города
        findButton = cityDialog.findViewById(R.id.city_menu_find_button)

        findButton.setOnClickListener {
            if (isSelectedCitiesValid()) { // проверка на валидность полей с городами
                saveValuesForCityDialog()
                cityDialog.dismiss()
                updateCityButton()
            }
        }

    }

    private fun initSavedValueCityDialogMap() { // важно чтобы получение сохраненных значений было после  initDatePickerCityDialog()
        // init map с дефолтными значениями
        savedValuesCityDialog.put(cityNameKeyCityDialog, "")
        savedValuesCityDialog.put(dateKeyCityDialog, getDateOfDeparture())
        savedValuesCityDialog.put(cityDepartureNameKeyCityDialog, "")
        savedValuesCityDialog.put(nightsKeyCityDialog, resources.getString(R.string.nights_value_city_layout))
        savedValuesCityDialog.put(peoplesKeyCityDialog, resources.getString(R.string.peoples_value_city_layout))
    }

    private fun saveValuesForCityDialog() { // сохранение значений из основных view из cityDialog в map
        savedValuesCityDialog.put(cityNameKeyCityDialog, cityField.text.toString())
        savedValuesCityDialog.put(dateKeyCityDialog, dateField.text.toString())
        savedValuesCityDialog.put(cityDepartureNameKeyCityDialog, cityDepartureField.text.toString())
        savedValuesCityDialog.put(nightsKeyCityDialog, nightsField.text.toString())
        savedValuesCityDialog.put(peoplesKeyCityDialog, peoplesField.text.toString())
    }

    private fun getSavedValuesForCityDialog() { // получение сохраненных значений для основных view в диалоге выбора города
        if (savedValuesCityDialog.isNotEmpty()) {
            cityField.setText(savedValuesCityDialog[cityNameKeyCityDialog].toString())
            dateField.setText(savedValuesCityDialog[dateKeyCityDialog].toString())
            cityDepartureField.setText(savedValuesCityDialog[cityDepartureNameKeyCityDialog].toString())
            nightsField.setText(savedValuesCityDialog[nightsKeyCityDialog].toString())
            peoplesField.setText(savedValuesCityDialog[peoplesKeyCityDialog].toString())
        }
    }


    private fun initCancelButtonCityDialog(cityDialog: Dialog) { // init кнопки "Отмена" в диалоге выбора города
        cityDialog.findViewById<Button>(R.id.city_menu_cancel_button).setOnClickListener {// кнопка "Отмена"
            cityDialog.cancel()
        }
    }

    private fun initNightsCounter(cityDialog: Dialog) { // инит количества ночей в диалоге выбора города
        val counterTextView = cityDialog.findViewById<EditText>(R.id.city_menu_nights_value)
        val plusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_nights_plus_button)
        val minusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_nights_minus_button)

        val minimumNumberOfNights = 1
        val maximumNumberOfNights = 30

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

        val minimumNumberOfPeople = 1
        val maximumNumberOfPeople = 10

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

    private fun initDatePickerCityDialog(cityDialog: Dialog) { // выбор даты вылета диалог
        lateinit var startDatePicker: DatePickerDialog
        var startDate: Date
        val textViewButtonDate = cityDialog.findViewById<TextView>(R.id.city_menu_date_picker_tv)
        textViewButtonDate.text = getDateOfDeparture() // дата вылета = завтра

//        textViewButtonDate.text = formatDate(Date().) // сегодняшняя дата

        // Создаем DatePickerDialog для выбора даты вылета
        startDatePicker = createDatePicker { selectedDate ->
            startDate = selectedDate
            val startDateStr = formatDate(startDate)
            textViewButtonDate.text = startDateStr
        }
        textViewButtonDate.setOnClickListener {
            startDatePicker.show()
        }
    }

    private fun getDateOfDeparture() = formatDate(calendar.time)

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

    private fun formatDate(date: Date?): String { // формат даты для выбора числа вылета
        val format = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return format.format(date)
    }

    private fun initMinDate() { // установка минимальной даты вылета
        calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1) // минимальная дата +1 (завтра)
        minYear = calendar.get(Calendar.YEAR)
        minMonth = calendar.get(Calendar.MONTH)
        minDay = calendar.get(Calendar.DAY_OF_MONTH)

    }

    private fun initButtonClearAutoCompleteCityDialog(cityDialog: Dialog) { // кнопка очистки рядом с полем выбора города, куда планируется тур
        val clearButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_clear_button)
        clearButton.setOnClickListener {
            cityDialog.findViewById<MaterialAutoCompleteTextView>(R.id.city_menu_autocomplete_tv).text.clear()
        }
    }

    private fun initButtonClearAutoCompleteDestinationCityDialog(cityDialog: Dialog) { // кнопка очистки рядом с полем выбора города, откуда вылет
        val clearButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_clear_button_city_from)
        clearButton.setOnClickListener {
            cityDialog.findViewById<MaterialAutoCompleteTextView>(R.id.city_menu_autocomplete_tv_city_from).text.clear()
        }
    }

    private fun initAutoCompleteCityDestinationCityDialog(cityDialog: Dialog) { // инит поля с выбором города, куда планируется тур

        val autoCompleteTextView = cityDialog.findViewById<MaterialAutoCompleteTextView>(R.id.city_menu_autocomplete_tv) // поле с выбором города
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        autoCompleteTextView.setAdapter(adapter)

    }

    private fun initAutoCompleteCityDialog(cityDialog: Dialog) { // инит поля с выбором города, откуда вылет

        val autoCompleteTextView =
            cityDialog.findViewById<MaterialAutoCompleteTextView>(R.id.city_menu_autocomplete_tv_city_from) // поле с выбором города
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        autoCompleteTextView.setAdapter(adapter)

    }

    private fun initTourAndHotelsButtonsCityDialog(cityDialog: Dialog) { // инициализация кнопок "Туры" и "Отели" в диалоге с выбором города и времени
        cityDialog.findViewById<Button>(R.id.city_menu_tours_button).setOnClickListener { // кнопка "Туры"
            switchStateButtonsToursAndHotels(true)

        }
        cityDialog.findViewById<Button>(R.id.city_menu_hotels_button).setOnClickListener { // кнопка "Отели"
            switchStateButtonsToursAndHotels(false)
        }
    }

    private fun isSelectedCitiesValid(): Boolean { // проверка на валидность полей с городами
        var isCityMainValid = false // флаг, правильно ли введено название города назначения
        var isCityDepartureValid = false // флаг, правильно ли введено название города вылета

        if (cityField.text.toString().isBlank() and cityDepartureField.text.toString().isBlank()) { // если оба поля пустые, то пропускаем
            return true
        }

        if (!isToursButtonActive) {
            if (cityField.text.toString().isBlank()
            ) return true // если активна "Отели", и поле города назначения пустое, то пропускаем, будет "Россия"
        }
        if (isToursButtonActive) {
            if (cityDepartureField.text.toString().isBlank()) { // если активна "Туры", а город вылета пустой, то пишем сообщение
                showSnackBarAboutValue("Введите название города отправления")
                return false
            }
        }


        if (isToursButtonActive) {
            if (cityField.text.toString() == cityDepartureField.text.toString()) { // проверка на совпадение городов назначения и вылета
                showSnackBarAboutValue("Города назначения и вылета не должны совпадать")
                return false
            }
        } else isCityDepartureValid = true

        for (item in cities) { // проверка, есть ли в списке городов город назначения
            if (item.toString() == cityField.text.toString()) isCityMainValid = true
        }
        if (isToursButtonActive) { // делаем проверку, только если кнопка "Туры" активна
            for (item in cities) { // проверка, есть ли в списке городов город вылета
                if (item.toString() == cityDepartureField.text.toString()) isCityDepartureValid = true
            }
        } else isCityDepartureValid = true


        if (isCityMainValid == true) {
            if (isCityDepartureValid == true) { // если все хорошо, возвращаем true
                return true
            }
        }

        showSnackBarAboutValue("Некорректное название города, пожалуйста, выберите из выпадающего списка")
        return false
    }

    private fun hideOrShowCityDepartureCityField() { // спрятать или показать поле ввода города вылета, в зависимости от того, активна ли кнопка "Туры"
        if (!isToursButtonActive) {
            cityDialog.findViewById<AutoCompleteTextView>(R.id.city_menu_autocomplete_tv_city_from).visibility =
                View.INVISIBLE // invisible, вместо gone, чтобы разметка не съезжала
            cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_clear_button_city_from).visibility = View.INVISIBLE
        } else {
            cityDialog.findViewById<AutoCompleteTextView>(R.id.city_menu_autocomplete_tv_city_from).visibility = View.VISIBLE
            cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_clear_button_city_from).visibility = View.VISIBLE
        }
    }

    private fun initSortingMenu() { // обработка меню сортировки

        val popupMenu = PopupMenu(requireContext(), binding.sortingButton)
        for (option in sortingStrings) {
            popupMenu.menu.add(option)
        }
        popupMenu.setOnMenuItemClickListener {// нажатие на один из элементов меню сортировки
            when (it.title) {
                sortingStrings[0] -> { // Рекомендуемое
                    // TODO Обработка выбранной сортировки
                    binding.sortingButton.text = it.title
                    true
                }

                sortingStrings[1] -> { // Сначала новое
                    // TODO Обработка выбранной сортировки
                    binding.sortingButton.text = it.title
                    true
                }

                sortingStrings[2] -> { // Дешевле
                    // TODO Обработка выбранной сортировки
                    binding.sortingButton.text = it.title
                    true
                }

                sortingStrings[3] -> { // Дороже
                    // TODO Обработка выбранной сортировки
                    binding.sortingButton.text = it.title
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
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

    private fun switchStateButtonsToursAndHotels(fromTours: Boolean) { // меняет активность кнопок "Туры" и "Отели" местами

        makeSwitchStateButtonsToursAndHotels(fromTours, true) // меняем цвета фона и цвета текста у кнопок "Туры" и "Отели"
        makeSwitchStateButtonsToursAndHotels(fromTours, false)
        hideOrShowCityDepartureCityField() // спрятать или показать поле ввода города вылета
        changeFindButtonText() // изменяет текст кнопки "Найти туры"/"Найти отели"

    }

    private fun changeFindButtonText() { // изменяет текст кнопки "Найти туры"/"Найти отели"(диалог выбора города) в зависимости от того, активна кнопка "Туры" или "Отели"
        if (!isToursButtonActive) {
            findButton.text = resources.getString(R.string.city_menu_find_button_text_find_hotels)
        } else {
            findButton.text = resources.getString(R.string.city_menu_find_button_text_find_tours)
        }
    }

    // fromTours: Boolean - это проверка на какую кнопку вызывается метод, чтобы не было повторных нажатий на одну и ту же кнопку (из "Туры" или "Отели");
    // isCallFromMainLayout: Boolean - в каком лэйауте красить кнопки, главный или в выборе города (нужно по 2 раза вызывать метод);
    private fun makeSwitchStateButtonsToursAndHotels(
        fromTours: Boolean,
        isCallForMainLayoutButtons: Boolean,
    ) { // метод меняет цвета фона и цвета текста у кнопок "Туры" и "Отели"

        lateinit var toursButton: MaterialButton
        lateinit var hotelsButton: MaterialButton

        if (isCallForMainLayoutButtons) { // если красим в main layout, то кнопки берем оттуда же
            toursButton = binding.toursButton
            hotelsButton = binding.hotelsButton
        } else { // если красим в city dialog, то кнопки берем оттуда же
            toursButton = cityDialog.findViewById(R.id.city_menu_tours_button)
            hotelsButton = cityDialog.findViewById(R.id.city_menu_hotels_button)
        }

        if (fromTours != isToursButtonActive) { // если клик не по той же кнопке
            if (!isToursButtonActive) {
                with(hotelsButton) {
                    if (isCallForMainLayoutButtons) setBackgroundColor(resources.getColor(R.color.color_for_background, null))
                    else {
                        setBackgroundColor(resources.getColor(R.color.white, null))
                    }
                    setTextColor(
                        resources.getColorStateList(
                            R.color.text_color_inactive_button,
                            null
                        )
                    )
                    icon.setTintList(
                        resources.getColorStateList(
                            R.color.text_color_inactive_button,
                            null
                        )
                    )
                }
                with(toursButton) {
                    if (isCallForMainLayoutButtons) setBackgroundColor(resources.getColor(R.color.background_color_for_active_buttons, null))
                    else {
                        setBackgroundColor(resources.getColor(R.color.city_menu_background_color_for_active_buttons, null))
                    }
                    setTextColor(
                        resources.getColorStateList(
                            R.color.text_color_active_button,
                            null
                        )
                    )
                    icon.setTintList(
                        resources.getColorStateList(
                            R.color.text_color_active_button,
                            null
                        )
                    )
                }
            } else {
                with(toursButton) {
                    if (isCallForMainLayoutButtons) setBackgroundColor(resources.getColor(R.color.color_for_background, null))
                    else {
                        setBackgroundColor(resources.getColor(R.color.white, null))
                    }
                    setTextColor(
                        resources.getColorStateList(
                            R.color.text_color_inactive_button,
                            null
                        )
                    )
                    icon.setTintList(
                        resources.getColorStateList(
                            R.color.text_color_inactive_button,
                            null
                        )
                    )
                }
                with(hotelsButton) {
                    if (isCallForMainLayoutButtons) {
                        setBackgroundColor(resources.getColor(R.color.background_color_for_active_buttons, null))
                    } else {
                        setBackgroundColor(resources.getColor(R.color.city_menu_background_color_for_active_buttons, null))
                    }
                    icon.setTintList(
                        resources.getColorStateList(
                            R.color.text_color_active_button,
                            null
                        )
                    )
                    setTextColor(
                        resources.getColorStateList(
                            R.color.text_color_active_button,
                            null
                        )
                    )
                }
            }

            if (!isCallForMainLayoutButtons) isToursButtonActive = !isToursButtonActive // меняем флаг активна ли кнопка "Туры"


        }
    }

    override fun init() {
//        vb?.rvUsers?.layoutManager = LinearLayoutManager(context)
//        adapter = UsersRVAdapter(presenter.usersListPresenter).apply {
//            userSubComponent?.inject(this)
//        }
//        vb?.rvUsers?.adapter = adapter
    }

    override fun updateList() {
//        adapter?.notifyDataSetChanged()
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