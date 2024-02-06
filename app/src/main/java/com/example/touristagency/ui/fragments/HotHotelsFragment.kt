package com.example.touristagency.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.subComponents.HotHotelsSubComponent
import com.example.touristagency.databinding.FragmentHotHotelsBinding
import com.example.touristagency.mvp.model.hotels.Hotel
import com.example.touristagency.mvp.presenter.HotHotelsPresenter
import com.example.touristagency.mvp.view.HotHotelsView
import com.example.touristagency.ui.activity.BackButtonListener
import com.example.touristagency.ui.adapter.HotelsRVAdapter
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class HotHotelsFragment(val hotTours: MutableList<Hotel>) : MvpAppCompatFragment(), HotHotelsView, BackButtonListener {
    private var _binding: FragmentHotHotelsBinding? = null
    private val binding get() = _binding!!

    private var hotToursSubComponent: HotHotelsSubComponent? = null


    private lateinit var currentCurrency: String // текущая валюта


    val presenter: HotHotelsPresenter by moxyPresenter {
        hotToursSubComponent = App.instance.initHotHotelsSubComponent()

        HotHotelsPresenter(hotTours).apply {
            hotToursSubComponent?.inject(this)
        }
    }

    var adapter: HotelsRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotHotelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initCurrentCurrency(currentCurrency: String) {
        this.currentCurrency = currentCurrency
    }

    override fun init() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = HotelsRVAdapter(presenter.toursListPresenter).apply {
            hotToursSubComponent?.inject(this)
        }
        binding.recyclerView.adapter = adapter
    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun release() {
        hotToursSubComponent = null
        App.instance.releaseHotHotelsSubComponent()
    }

    override fun initBottomNavigationMenu() {
        binding.bottomNavigation.menu.findItem(R.id.item_hot_tours).isChecked = true
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


    override fun setTextSortingButton(text: String) {
        binding.sortingButton.text = text
    }

    override fun initSortingButton() {
        binding.sortingButton.setOnClickListener {
            presenter.sortingButtonOnClick()
        }
    }

    override fun initSortingMenu(sortingStrings: List<String>) { // обработка меню сортировки
        val popupMenu = PopupMenu(requireContext(), binding.sortingButton)
        for (option in sortingStrings) {
            popupMenu.menu.add(option)
        }
        popupMenu.setOnMenuItemClickListener {// нажатие на один из элементов меню сортировки
            when (it.title) {
                sortingStrings[0] -> { // Рекомендуемое
                    presenter.sortingItemOnClick(it)
                    true
                }

                sortingStrings[1] -> { // Сначала новое
                    presenter.sortingItemOnClick(it)
                    true
                }

                sortingStrings[2] -> { // Дешевле
                    presenter.sortingItemOnClick(it)
                    true
                }

                sortingStrings[3] -> { // Дороже
                    presenter.sortingItemOnClick(it)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    override fun updateImage(position: Int) {
        val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(position)
        if (viewHolder != null && viewHolder is HotelsRVAdapter.ViewHolder) {
            viewHolder.favouriteImageView.setImageResource(R.drawable.baseline_favorite_24)
        }
    }


    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance(hotTours: MutableList<Hotel>) = HotHotelsFragment(hotTours)
    }


}