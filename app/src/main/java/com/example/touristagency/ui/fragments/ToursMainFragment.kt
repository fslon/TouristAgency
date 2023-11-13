package com.example.touristagency.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
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
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

// https://www.youtube.com/watch?v=pl_ta6SVhm4
class ToursMainFragment : MvpAppCompatFragment(), ToursView, BackButtonListener {
    private var _binding: FragmentToursMainBinding? = null
    private val binding get() = _binding!!

    private var toursSubComponent: ToursSubComponent? = null

    private var isToursButtonActive = true // флаг, активна ли кнопка "Туры"

    private val sortingStrings = listOf("Рекомендуемое", "Сначала новое", "Дешевле", "Дороже") // способы сортировки для меню сортировки

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

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