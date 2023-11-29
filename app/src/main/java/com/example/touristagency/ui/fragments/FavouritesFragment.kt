package com.example.touristagency.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.FavouritesSubComponent
import com.example.touristagency.databinding.FragmentFavouritesBinding
import com.example.touristagency.mvp.presenter.FavouritesPresenter
import com.example.touristagency.mvp.view.FavouritesView
import com.example.touristagency.mvp.view.HotToursView
import com.example.touristagency.mvp.view.SlideShowAdapter
import com.example.touristagency.ui.activity.BackButtonListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class FavouritesFragment : MvpAppCompatFragment(), FavouritesView, BackButtonListener {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private var favouritesSubComponent: FavouritesSubComponent? = null


    private lateinit var currentCurrency: String // текущая валюта


    val presenter: FavouritesPresenter by moxyPresenter {
        favouritesSubComponent = App.instance.initFavouritesSubComponent()

        FavouritesPresenter().apply {
            favouritesSubComponent?.inject(this)
        }
    }

//    var adapter: UsersRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initCurrentCurrency(currentCurrency: String) {
        this.currentCurrency = currentCurrency
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
        favouritesSubComponent = null
        App.instance.releaseFavouritesSubComponent()
    }

    override fun initBottomNavigationMenu() {
        binding.bottomNavigation.menu.findItem(R.id.item_favourite).isChecked = true
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
                    println("item_profile")
                    return@setOnItemSelectedListener true
                }

                else -> {
                    false
                }
            }

        }
    }


    override fun testInitFirstRecyclerItem() { // прокручивающиеся картинки в recyclerViewItem // todo переработать

        binding.testItem.recyclerItemTourHotelName.text = "«Бургас» пансионат"
        binding.testItem.recyclerItemTourHotelLocation.text = "г. Сочи, п. Кудепста"
        binding.testItem.recyclerItemTourHotelRating.text = "9.1"

        binding.testItem.recyclerItemTourAirportTextView.text = "в 6 км"
        binding.testItem.recyclerItemTourBeachTextView.text = "150 м"

        binding.testItem.recyclerItemTourPriceTextView.text = "96 583 $currentCurrency"


        binding.testItem.recyclerItemTourFavouriteButton.setOnClickListener {
            // todo прокинуть метод в presenter
        }
        // Создаем список изображений
        val images = listOf(
            R.drawable.burgas_1,
            R.drawable.burgas_2,
            R.drawable.burgas_3
        )

        binding.testItem.recyclerItemTourLineImage.setImageResource(R.drawable.first_24)

// Создаем экземпляр PagerAdapter и устанавливаем его во ViewPager2
        val adapter = SlideShowAdapter(images)
        binding.testItem.recyclerItemTourImageLayoutViewpager2.adapter = adapter

        binding.testItem.recyclerItemTourFavouriteButton.setImageResource(R.drawable.baseline_favorite_24)

        binding.testItem.recyclerItemTourFavouriteButton.setOnClickListener {
            binding.testItem.recyclerItemTourFavouriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
        }

    }

    override fun testInitSecondRecyclerItem() {
        binding.testItem2.recyclerItemTourLineImage.setImageResource(R.drawable.first_24)

        binding.testItem2.recyclerItemTourHotelName.text = "«Сочи Парк Отель»"
        binding.testItem2.recyclerItemTourHotelLocation.text = "Краснодарский край, пгт. Сириус"
        binding.testItem2.recyclerItemTourHotelRating.text = "9.6"

        binding.testItem2.recyclerItemTourAirportTextView.text = "в 5 км"
        binding.testItem2.recyclerItemTourBeachTextView.text = "1000 м"

        binding.testItem2.recyclerItemTourPriceTextView.text = "121 254 $currentCurrency"


        binding.testItem2.recyclerItemTourParkingTextView.visibility = View.GONE
        binding.testItem2.recyclerItemTourParkingImage.visibility = View.GONE

        binding.testItem2.recyclerItemTourFavouriteButton.setOnClickListener {
            // todo прокинуть метод в presenter
        }
        // Создаем список изображений
        val images = listOf(
            R.drawable.sochi_hotel_1,
            R.drawable.sochi_hotel_2,
            R.drawable.sochi_hotel_3,

            )

// Создаем экземпляр PagerAdapter и устанавливаем его во ViewPager2
        val adapter = SlideShowAdapter(images)
        binding.testItem2.recyclerItemTourImageLayoutViewpager2.adapter = adapter

        binding.testItem2.recyclerItemTourFavouriteButton.setImageResource(R.drawable.baseline_favorite_24)

        binding.testItem2.recyclerItemTourFavouriteButton.setOnClickListener {
            binding.testItem2.recyclerItemTourFavouriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
        }

    }


    override fun testInitThirdRecyclerItem() {

        binding.testItem3.recyclerItemTourHotelName.text = "«Алеан Фэмили Спутник» отель"
        binding.testItem3.recyclerItemTourHotelLocation.text = "г.Сочи, Новороссийское шоссе, д. 17/1"
        binding.testItem3.recyclerItemTourHotelRating.text = "9.2"

        binding.testItem3.recyclerItemTourLineImage.setImageResource(R.drawable.second_24)
        binding.testItem3.recyclerItemTourAirportTextView.text = "в 15 км"
        binding.testItem3.recyclerItemTourBeachTextView.text = "150 м"

        binding.testItem3.recyclerItemTourPriceTextView.text = "152 965 $currentCurrency"


//        binding.testItem2.recyclerItemTourParkingTextView.visibility = View.GONE
//        binding.testItem2.recyclerItemTourParkingImage.visibility = View.GONE

        binding.testItem3.recyclerItemTourFavouriteButton.setOnClickListener {
            // todo прокинуть метод в presenter
        }
        // Создаем список изображений
        val images = listOf(
            R.drawable.alean_sochi_1,
            R.drawable.alean_sochi_2,
            R.drawable.alean_sochi_3,

            )

// Создаем экземпляр PagerAdapter и устанавливаем его во ViewPager2
        val adapter = SlideShowAdapter(images)
        binding.testItem3.recyclerItemTourImageLayoutViewpager2.adapter = adapter


        binding.testItem3.recyclerItemTourFavouriteButton.setImageResource(R.drawable.baseline_favorite_24)

        binding.testItem3.recyclerItemTourFavouriteButton.setOnClickListener {
            binding.testItem3.recyclerItemTourFavouriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
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


    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() = FavouritesFragment()
    }


}