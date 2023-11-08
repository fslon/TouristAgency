package com.example.touristagency.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.widget.PopupMenu
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.ToursSubComponent
import com.example.touristagency.databinding.FragmentToursMainBinding
import com.example.touristagency.mvp.presenter.ToursMainPresenter
import com.example.touristagency.mvp.view.ToursView
import com.example.touristagency.ui.activity.BackButtonListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

// https://www.youtube.com/watch?v=pl_ta6SVhm4
class ToursMainFragment : MvpAppCompatFragment(), ToursView, BackButtonListener {
    private var _binding: FragmentToursMainBinding? = null
    private val binding get() = _binding!!

    private var toursSubComponent: ToursSubComponent? = null

    private var isToursButtonActive = true

    val sortingStrings = listOf("Рекомендуемое", "Сначала новое", "Дешевле", "Дороже")
//    lateinit var adapterItems: ArrayAdapter<String>

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

        binding.toursButton.setOnClickListener {
            switchStateButtonsToursAndHotels(true)
        }

        binding.hotelsButton.setOnClickListener {
            switchStateButtonsToursAndHotels(false)
        }


        binding.sortingButton.text = sortingStrings[0] // дефолтный способ сортировки
        binding.sortingButton.setOnClickListener { initSortingMenu() }

        binding.filtersButton.setOnClickListener { showBottomDialog() }


    }

    private fun initSortingMenu() {

        val popupMenu = PopupMenu(requireContext(), binding.sortingButton)
        for (option in sortingStrings) {
            popupMenu.menu.add(option)
        }
        popupMenu.setOnMenuItemClickListener {
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

    private fun showBottomDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheet_layout)

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(
            Gravity.BOTTOM
        )


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
                    setBackgroundColor(resources.getColor(R.color.white, null))
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
                    setBackgroundColor(resources.getColor(R.color.white, null))
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