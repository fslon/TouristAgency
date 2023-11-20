package com.example.touristagency.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.ToursSubComponent
import com.example.touristagency.databinding.FragmentToursMainBinding
import com.example.touristagency.mvp.presenter.ToursMainPresenter
import com.example.touristagency.mvp.view.SlideShowAdapter
import com.example.touristagency.mvp.view.ToursView
import com.example.touristagency.ui.activity.BackButtonListener
import com.example.touristagency.ui.activity.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
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

    private lateinit var filtersDialog: BottomSheetDialog // диалог с фильтрами

    private lateinit var currentCurrency: String // текущая валюта

    private var minYear = 0 // минимальный год для выбора даты вылета
    private var minMonth = 0 // минимальный месяц для выбора даты вылета
    private var minDay = 0 // минимальный день для выбора даты вылета
    private lateinit var calendar: Calendar // календарь для выбора даты вылета в меню выбора города


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

        currentCurrency = resources.getString(R.string.current_currency)
        initMinDate()


        initCityDialog()
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


        testinit()
        testinit2()

    }

    private fun testinit() {


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
        binding.testItem.viewPager2.adapter = adapter

    }

    private fun testinit2() {

        binding.testItem2.tv1.text = "Санаторий \"Надежда\""
        binding.testItem2.tv2.text = "Анапа, Россия"
        binding.testItem2.tv3.text = "9.0"

        binding.testItem2.firstImage.setImageResource(R.drawable.second_24)
        binding.testItem2.airportValue.text = "в 1 км"
        binding.testItem2.beachValue.text = "650 м"

        binding.testItem2.price.text = "40 627 ₽"


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
        binding.testItem2.viewPager2.adapter = adapter

    }

    private fun initFiltersDialog() { // init меню фильтров

        val bottomSheet = layoutInflater.inflate(R.layout.filters_bottom_sheet_layout, null)
        filtersDialog = BottomSheetDialog(requireContext())
        filtersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filtersDialog.setContentView(bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet.parent as View)
        bottomSheetBehavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.dialog_filters_peek_height) // Устанавливаем высоту, на которую откроется BottomSheet


        // блок с "ценой" в фильтрах
        //todo сделать получение сохраненных значений
        val priceNumberFrom = filtersDialog.findViewById<TextView>(R.id.price_number_from) // textView с ценой "от"
        val priceNumberTo = filtersDialog.findViewById<TextView>(R.id.price_number_to) // textView с ценой "до"
        priceNumberFrom?.text = resources.getString(R.string.price_number_from_default) + currentCurrency // дефолтное значение
        priceNumberTo?.text = resources.getString(R.string.price_number_to_default) + currentCurrency // дефолтное значение

        val rangeSlider = filtersDialog.findViewById<RangeSlider>(R.id.price_slider) // слайдер с ценой
        rangeSlider?.addOnChangeListener { slider, value, fromUser -> // изменения в слайдере с ценой
            priceNumberFrom?.text = slider.values[0].toInt().toString() + currentCurrency // изменение подписи цены под слайдером
            priceNumberTo?.text = slider.values[1].toInt().toString() + currentCurrency // изменение подписи цены под слайдером
        }

        // блок с "количеством звезд" в фильтрах
        //todo сделать получение сохраненных значений
        val starsNumberFrom = filtersDialog.findViewById<TextView>(R.id.stars_number_from) // textView с ценой "от"
        val starsNumberTo = filtersDialog.findViewById<TextView>(R.id.stars_number_to) // textView с ценой "до"
        starsNumberFrom?.text = resources.getString(R.string.stars_number_from_default)  // дефолтное значение
        starsNumberTo?.text = resources.getString(R.string.stars_number_to_default) // дефолтное значение

        val starsSlider = filtersDialog.findViewById<RangeSlider>(R.id.stars_slider) // слайдер со звездами
        starsSlider?.addOnChangeListener { slider, value, fromUser -> // изменения в слайдере со звездами
            starsNumberFrom?.text = slider.values[0].toInt().toString()  // изменение подписи звезд под слайдером
            starsNumberTo?.text = slider.values[1].toInt().toString() // изменение подписи звезд под слайдером
        }

        initInfrastructureCheckboxes(filtersDialog)



        filtersDialog.setOnCancelListener { // listener на закрытие диалога с фильтрами
            starsSlider?.values
            // TODO: Сделать тут получение данных из фильтров, с последующей обработкой и сохранением
        }


    }


    private fun initCityDialog() { // инициализация диалога с выбором города и даты
        val citySheet = layoutInflater.inflate(R.layout.city_sheet_layout, null)
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

        initCancelButtonCityDialog(cityDialog) // кнопка "Отмена"

        initTourAndHotelsButtonsCityDialog(cityDialog) // кнопки "Туры", "Отели"

        initAutoCompleteCityDialog(cityDialog) // выбор города
        initButtonClearAutoCompleteCityDialog(cityDialog) // очистить поле выбора города

        initAutoCompleteCityDestinationCityDialog(cityDialog) // выбор города откуда вылет
        initButtonClearAutoCompleteDestinationCityDialog(cityDialog) // очистить поле выбора города откуда вылет

        initDatePickerCityDialog(cityDialog) // выбор даты

        initNightsCounter(cityDialog) // количество ночей
        initPeoplesCounter(cityDialog) // количество людей


        cityDialog.setOnCancelListener { // listener на закрытие диалога с фильтрами
            val test = cityDialog.findViewById<MaterialButton>(R.id.city_menu_hotels_button)
//            Log.e("//////////// ",test.iconTint.toString() )

            cityDialog.dismiss()

//            starsSlider?.values
//            peoplesSlider?.value
            // TODO: Сделать тут получение данных из диалога выбора города, с последующей обработкой и сохранением
        }
    }


    private fun initCancelButtonCityDialog(cityDialog: Dialog) {
        cityDialog.findViewById<Button>(R.id.city_menu_cancel_button).setOnClickListener {// кнопка "Отмена"
            cityDialog.dismiss()
        }
    }

    private fun initNightsCounter(cityDialog: Dialog) { // инит количества ночей в меню выбора города
        val counterTextView = cityDialog.findViewById<EditText>(R.id.city_menu_nights_value)
        val plusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_nights_plus_button)
        val minusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_nights_minus_button)

        counterTextView.addTextChangedListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString().isNotEmpty()) {
                if (counterTextView.text.toString().toInt() > 30) counterTextView.setText("30")
                if (counterTextView.text.toString().toInt() < 1) counterTextView.setText("1")
                counterTextView.setSelection(counterTextView.text.length)
            }
        }
        plusButton.setOnClickListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString()
                    .isNotEmpty()
            ) counterTextView.setText((counterTextView.text.toString().toInt() + 1).toString())
            else counterTextView.setText("1")
            counterTextView.setSelection(counterTextView.text.length)
        }
        minusButton.setOnClickListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString()
                    .isNotEmpty()
            ) counterTextView.setText((counterTextView.text.toString().toInt() - 1).toString())
            else counterTextView.setText("1")
            counterTextView.setSelection(counterTextView.text.length)
        }
    }

    private fun initPeoplesCounter(cityDialog: Dialog) { // инит количества людей в меню выбора города
        val counterTextView = cityDialog.findViewById<EditText>(R.id.city_menu_peoples_value)
        val plusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_peoples_plus_button)
        val minusButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_peoples_minus_button)

        counterTextView.addTextChangedListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString().isNotEmpty()) {
                if (counterTextView.text.toString().toInt() > 10) counterTextView.setText("10")
                if (counterTextView.text.toString().toInt() < 1) counterTextView.setText("1")
                counterTextView.setSelection(counterTextView.text.length)
            }
        }
        plusButton.setOnClickListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString()
                    .isNotEmpty()
            ) counterTextView.setText((counterTextView.text.toString().toInt() + 1).toString())
            else counterTextView.setText("1")
            counterTextView.setSelection(counterTextView.text.length)
        }
        minusButton.setOnClickListener {
            if (counterTextView.text.toString().isNotBlank() and counterTextView.text.toString()
                    .isNotEmpty()
            ) counterTextView.setText((counterTextView.text.toString().toInt() - 1).toString())
            else counterTextView.setText("1")
            counterTextView.setSelection(counterTextView.text.length)
        }
    }

    private fun initDatePickerCityDialog(cityDialog: Dialog) { // выбор даты вылета диалог
        lateinit var startDatePicker: DatePickerDialog
        var startDate: Date
        val textViewButtonDate = cityDialog.findViewById<TextView>(R.id.city_menu_date_picker_tv)
        textViewButtonDate.text = formatDate(calendar.time) // дата вылета = завтра

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
        val cities = listOf( // todo вынести в файл с массивами может быть
            "Анапа",
            "Москва",
            "Санкт-Петербург",
            "Новосибирск",
            "Екатеринбург",
            "Нижний Новгород",
            "Казань",
            "Челябинск",
            "Омск",
            "Самара",
            "Ростов-на-Дону",
            "Уфа",
            "Красноярск",
            "Воронеж",
            "Пермь",
            "Волгоград",
            "Краснодар",
            "Саратов",
            "Тюмень",
            "Тольятти",
            "Ижевск",
            "Барнаул",
            "Иркутск",
            "Ульяновск",
            "Владивосток",
            "Ярославль",
            "Хабаровск",
            "Махачкала",
            "Оренбург",
            "Томск",
            "Новокузнецк",
            "Кемерово",
            "Рязань",
            "Астрахань",
            "Набережные Челны",
            "Пенза",
            "Липецк",
            "Тула",
            "Киров",
            "Чебоксары",
            "Калининград",
            "Курск",
            "Улан-Удэ",
            "Тверь",
            "Ставрополь",
            "Магнитогорск",
            "Брянск",
            "Белгород",
            "Архангельск",
            "Ангарск",
            "Смоленск"
        )

        val autoCompleteTextView = cityDialog.findViewById<MaterialAutoCompleteTextView>(R.id.city_menu_autocomplete_tv) // поле с выбором города
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        autoCompleteTextView.setAdapter(adapter)

    }

    private fun initAutoCompleteCityDialog(cityDialog: Dialog) { // инит поля с выбором города, откуда вылет
        val cities = listOf( // todo вынести в файл с массивами может быть
            "Анапа",
            "Москва",
            "Санкт-Петербург",
            "Новосибирск",
            "Екатеринбург",
            "Нижний Новгород",
            "Казань",
            "Челябинск",
            "Омск",
            "Самара",
            "Ростов-на-Дону",
            "Уфа",
            "Красноярск",
            "Воронеж",
            "Пермь",
            "Волгоград",
            "Краснодар",
            "Саратов",
            "Тюмень",
            "Тольятти",
            "Ижевск",
            "Барнаул",
            "Иркутск",
            "Ульяновск",
            "Владивосток",
            "Ярославль",
            "Хабаровск",
            "Махачкала",
            "Оренбург",
            "Томск",
            "Новокузнецк",
            "Кемерово",
            "Рязань",
            "Астрахань",
            "Набережные Челны",
            "Пенза",
            "Липецк",
            "Тула",
            "Киров",
            "Чебоксары",
            "Калининград",
            "Курск",
            "Улан-Удэ",
            "Тверь",
            "Ставрополь",
            "Магнитогорск",
            "Брянск",
            "Белгород",
            "Архангельск",
            "Ангарск",
            "Смоленск"
        )

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
                with(filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_1)) {
                    this?.isChecked = !this?.isChecked!! // при нажатии на TextView, меняется состояние чекбокса рядом
                }
            }

        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_2)?.setOnClickListener {
            with(filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_2)) {
                this?.isChecked = !this?.isChecked!!
            }
        }

        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_3)?.setOnClickListener {
            with(filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_3)) {
                this?.isChecked = !this?.isChecked!!
            }
        }

        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_4)?.setOnClickListener {
            with(filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_4)) {
                this?.isChecked = !this?.isChecked!!
            }
        }

        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_5)?.setOnClickListener {
            with(filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_5)) {
                this?.isChecked = !this?.isChecked!!
            }
        }

        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_6)?.setOnClickListener {
            with(filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_6)) {
                this?.isChecked = !this?.isChecked!!
            }
        }

        filtersDialog.findViewById<TextView>(R.id.filters_infrastructure_textview_7)?.setOnClickListener {
            with(filtersDialog.findViewById<MaterialCheckBox>(R.id.filters_infrastructure_checkbox_7)) {
                this?.isChecked = !this?.isChecked!!
            }
        }


    }

    private fun switchStateButtonsToursAndHotels(fromTours: Boolean) {

        makeSwitchStateButtonsToursAndHotels(fromTours, true)
        makeSwitchStateButtonsToursAndHotels(fromTours, false)
        hideOrShowCityDepartureCityField()

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
                    Log.e("---------- ", this.iconTint.toString())
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