package com.example.touristagency.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.ToursSubComponent
import com.example.touristagency.databinding.FragmentToursMainBinding
import com.example.touristagency.mvp.presenter.ToursMainPresenter
import com.example.touristagency.mvp.view.ToursView
import com.example.touristagency.ui.activity.BackButtonListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ToursMainFragment : MvpAppCompatFragment(), ToursView, BackButtonListener {
    private var _binding: FragmentToursMainBinding? = null
    private val binding get() = _binding!!

    private var toursSubComponent: ToursSubComponent? = null

    private var isToursButtonActiveMainLayout = true // флаг, активна ли кнопка "Туры" в основом лэйауте
    private var isToursButtonActiveCityLayout = true // флаг, активна ли кнопка "Туры" в меню выбора города

    private val sortingStrings = listOf("Рекомендуемое", "По рейтингу", "Дешевле", "Дороже") // способы сортировки для меню сортировки

    private lateinit var cityDialog: Dialog // диалог с выбором города и даты

    private lateinit var filtersDialog: BottomSheetDialog // диалог с фильтрами

    private lateinit var currentCurrency: String // текущая валюта

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



        initCityDialog()
        binding.cityAndNightsButton.root.setOnClickListener {
//            showCityDialog()
            cityDialog.show()
        }

        binding.toursButton.setOnClickListener { // кнопка "Туры"
            switchStateButtonsToursAndHotels(true, true, isToursButtonActiveMainLayout, binding.toursButton, binding.hotelsButton)
        }

        binding.hotelsButton.setOnClickListener { // кнопка "Отели"
            switchStateButtonsToursAndHotels(false, true, isToursButtonActiveMainLayout, binding.toursButton, binding.hotelsButton)
        }


        binding.sortingButton.text = sortingStrings[0] // присвоение дефолтного способа сортировки
        binding.sortingButton.setOnClickListener { initSortingMenu() } // кнопка "Сортировка"

        initFiltersDialog()
        binding.filtersButton.setOnClickListener {// кнопка "Фильтры"
            filtersDialog.show()
        }


    }


    private fun initCityDialog() { // инициализация диалога с выбором города и даты
        val citySheet = layoutInflater.inflate(R.layout.city_sheet_layout, null)
        cityDialog = Dialog(requireContext(), R.style.CityDateDialogStyle)
        cityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        cityDialog.setContentView(citySheet)
//        cityDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.dialog_city_peek_height))

        val window: Window = cityDialog.window!!
        val param: WindowManager.LayoutParams = window.attributes
        param.width = WindowManager.LayoutParams.MATCH_PARENT
        param.height = resources.getDimensionPixelSize(R.dimen.dialog_city_peek_height)
        param.gravity = Gravity.TOP
        window.attributes = param


        cityDialog.setOnCancelListener { // listener на закрытие диалога с фильтрами
            cityDialog.dismiss()
//            starsSlider?.values
//            peoplesSlider?.value
            // TODO: Сделать тут получение данных из фильтров, с последующей обработкой и сохранением
        }

        cityDialog.findViewById<Button>(R.id.city_menu_cancel_button).setOnClickListener {// кнопка "Отмена" // todo вынести что ли отсюда
            cityDialog.dismiss()
        }

        initTourAndHotelsButtonsCityDialog(cityDialog)
        initAutoCompleteCityDialog(cityDialog)
        initButtonClearAutoCompleteCityDialog(cityDialog)
    }

    private fun initButtonClearAutoCompleteCityDialog(cityDialog: Dialog) { // кнопка очистки рядом с полем выбора города
        val clearButton = cityDialog.findViewById<AppCompatImageButton>(R.id.city_menu_clear_button)
        clearButton.setOnClickListener {
            cityDialog.findViewById<MaterialAutoCompleteTextView>(R.id.city_menu_autocomplete_tv).text.clear()
        }
    }

    private fun initAutoCompleteCityDialog(cityDialog: Dialog) { // инит поля с выбором города
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

    private fun initTourAndHotelsButtonsCityDialog(cityDialog: Dialog) { // инициализация кнопок "Туры" и "Отели" в диалоге с выбором города и времени
        cityDialog.findViewById<Button>(R.id.city_menu_tours_button).setOnClickListener { // кнопка "Туры"
            switchStateButtonsToursAndHotels(
                true,
                false,
                isToursButtonActiveCityLayout,
                cityDialog.findViewById(R.id.city_menu_tours_button),
                cityDialog.findViewById(R.id.city_menu_hotels_button)
            )
        }
        cityDialog.findViewById<Button>(R.id.city_menu_hotels_button).setOnClickListener { // кнопка "Отели"
            switchStateButtonsToursAndHotels(
                false,
                false,
                isToursButtonActiveCityLayout,
                cityDialog.findViewById(R.id.city_menu_tours_button),
                cityDialog.findViewById(R.id.city_menu_hotels_button)
            )
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

    private fun initFiltersDialog() { // показать меню фильтров

        val bottomSheet = layoutInflater.inflate(R.layout.filters_bottom_sheet_layout, null)
        filtersDialog = BottomSheetDialog(requireContext())
        filtersDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filtersDialog.setContentView(bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet.parent as View)
        bottomSheetBehavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.dialog_filters_peek_height) // Устанавливаем высоту, на которую откроется BottomSheet
//        filtersDialog.show()

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

        // блок с "количеством человек" в фильтрах
        //todo сделать получение сохраненных значений
//        val peoplesNumberFrom = dialog.findViewById<TextView>(R.id.peoples_number_from) // textView с ценой "от"
//        val peoplesNumberTo = dialog.findViewById<TextView>(R.id.peoples_number_to) // textView с ценой "до"
//        peoplesNumberFrom?.text = resources.getString(R.string.peoples_number_from_default)  // дефолтное значение
//        peoplesNumberTo?.text = resources.getString(R.string.peoples_number_to_default) // дефолтное значение

        val peoplesSlider = filtersDialog.findViewById<Slider>(R.id.peoples_slider) // слайдер с колвом человек
        peoplesSlider?.addOnChangeListener { slider, value, fromUser -> // изменения в слайдере с колвом человек
//            peoplesNumberFrom?.text = slider.values[0].toInt().toString()  // изменение подписи человек под слайдером
//            peoplesNumberTo?.text = slider.values[1].toInt().toString() // изменение подписи человек под слайдером
        }

        initInfrastructureCheckboxes(filtersDialog)



        filtersDialog.setOnCancelListener { // listener на закрытие диалога с фильтрами
            starsSlider?.values
            peoplesSlider?.value
            // TODO: Сделать тут получение данных из фильтров, с последующей обработкой и сохранением
        }


    }

    private fun initInfrastructureCheckboxes(filtersDialog: BottomSheetDialog) { // init checkboxes + textviews
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

    // fromTours: Boolean - это проверка на какую кнопку вызывается метод, чтобы не было повторных нажатий на одну и ту же кнопку (из "Туры" или "Отели");
    // isCallFromMainLayout: Boolean - из главного лэйаута, или из выбора города;
    // isToursButtonActive: Boolean - активна ли кнопка "Туры" [есть разница из какого лэйаута кнопка]
    private fun switchStateButtonsToursAndHotels(
        fromTours: Boolean,
        isCallFromMainLayout: Boolean,
        isToursButtonActive: Boolean,
        toursButton: MaterialButton,
        hotelsButton: MaterialButton
    ) { // метод меняет цвета фона и цвета текста у кнопок "Туры" и "Отели

        if (fromTours != isToursButtonActive) { // если клик не по той же кнопке
            if (!isToursButtonActive) {
                with(hotelsButton) {
                    if (isCallFromMainLayout) setBackgroundColor(resources.getColor(R.color.color_for_background, null))
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
                    if (isCallFromMainLayout) setBackgroundColor(resources.getColor(R.color.background_color_for_active_buttons, null))
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
                    if (isCallFromMainLayout) setBackgroundColor(resources.getColor(R.color.color_for_background, null))
                    if (!isCallFromMainLayout) {
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
                    if (isCallFromMainLayout) setBackgroundColor(resources.getColor(R.color.background_color_for_active_buttons, null))
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
            }


            if (isCallFromMainLayout) { // меняем флаги активна ли кнопка "Туры" в зависимости от лэйаута, откуда был запрос
                isToursButtonActiveMainLayout = !isToursButtonActiveMainLayout
            } else {
                isToursButtonActiveCityLayout = !isToursButtonActiveCityLayout
            }

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