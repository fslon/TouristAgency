package com.example.touristagency.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
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
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ToursMainFragment : MvpAppCompatFragment(), ToursView, BackButtonListener {
    private var _binding: FragmentToursMainBinding? = null
    private val binding get() = _binding!!

    private var toursSubComponent: ToursSubComponent? = null

    private var isToursButtonActive = true // флаг, активна ли кнопка "Туры"

    private val sortingStrings = listOf("Рекомендуемое", "По рейтингу", "Дешевле", "Дороже") // способы сортировки для меню сортировки

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

        binding.toursButton.setOnClickListener { // кнопка "Туры"
            switchStateButtonsToursAndHotels(true)
        }

        binding.hotelsButton.setOnClickListener { // кнопка "Отели"
            switchStateButtonsToursAndHotels(false)
        }


        binding.sortingButton.text = sortingStrings[0] // присвоение дефолтного способа сортировки
        binding.sortingButton.setOnClickListener { initSortingMenu() } // кнопка "Сортировка"

        binding.filtersButton.setOnClickListener {// кнопка "Фильтры"
            showBottomDialog()
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

    private fun showBottomDialog() { // показать меню фильтров

        val bottomSheet = layoutInflater.inflate(R.layout.filters_bottom_sheet_layout, null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet.parent as View)
        bottomSheetBehavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.dialog_filters_peek_height) // Устанавливаем высоту, на которую откроется BottomSheet
        dialog.show()

        // блок с "ценой" в фильтрах
        //todo сделать получение сохраненных значений
        val priceNumberFrom = dialog.findViewById<TextView>(R.id.price_number_from) // textView с ценой "от"
        val priceNumberTo = dialog.findViewById<TextView>(R.id.price_number_to) // textView с ценой "до"
        priceNumberFrom?.text = resources.getString(R.string.price_number_from_default) + currentCurrency // дефолтное значение
        priceNumberTo?.text = resources.getString(R.string.price_number_to_default) + currentCurrency // дефолтное значение

        val rangeSlider = dialog.findViewById<RangeSlider>(R.id.price_slider) // слайдер с ценой
        rangeSlider?.addOnChangeListener { slider, value, fromUser -> // изменения в слайдере с ценой
            priceNumberFrom?.text = slider.values[0].toInt().toString() + currentCurrency // изменение подписи цены под слайдером
            priceNumberTo?.text = slider.values[1].toInt().toString() + currentCurrency // изменение подписи цены под слайдером
        }

        // блок с "количеством звезд" в фильтрах
        //todo сделать получение сохраненных значений
        val starsNumberFrom = dialog.findViewById<TextView>(R.id.stars_number_from) // textView с ценой "от"
        val starsNumberTo = dialog.findViewById<TextView>(R.id.stars_number_to) // textView с ценой "до"
        starsNumberFrom?.text = resources.getString(R.string.stars_number_from_default)  // дефолтное значение
        starsNumberTo?.text = resources.getString(R.string.stars_number_to_default) // дефолтное значение

        val starsSlider = dialog.findViewById<RangeSlider>(R.id.stars_slider) // слайдер со звездами
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

        val peoplesSlider = dialog.findViewById<Slider>(R.id.peoples_slider) // слайдер с колвом человек
        peoplesSlider?.addOnChangeListener { slider, value, fromUser -> // изменения в слайдере с колвом человек
//            peoplesNumberFrom?.text = slider.values[0].toInt().toString()  // изменение подписи человек под слайдером
//            peoplesNumberTo?.text = slider.values[1].toInt().toString() // изменение подписи человек под слайдером
        }


        dialog.setOnCancelListener { // listener на закрытие диалога с фильтрами
            starsSlider?.values
            peoplesSlider?.value
            // TODO: Сделать тут получение данных из фильтров, с последующей обработкой и сохранением
        }


    }

    private fun switchStateButtonsToursAndHotels(fromTours: Boolean) { // boolean это проверка на какую кнопку вызывается метод, чтобы не было повторных нажатий на одну и ту же кнопку
        if (fromTours != isToursButtonActive) { // если клик не по той же кнопке
            if (!isToursButtonActive) {
                with(binding.hotelsButton) {
                    setBackgroundColor(resources.getColor(R.color.color_for_background, null))
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
                with(binding.toursButton) {
                    setBackgroundColor(resources.getColor(R.color.background_color_for_active_buttons, null))
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
                with(binding.toursButton) {
                    setBackgroundColor(resources.getColor(R.color.color_for_background, null))
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
                with(binding.hotelsButton) {
                    setBackgroundColor(resources.getColor(R.color.background_color_for_active_buttons, null))
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

            isToursButtonActive = !isToursButtonActive
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