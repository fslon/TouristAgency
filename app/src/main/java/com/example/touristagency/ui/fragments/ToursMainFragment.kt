package com.example.touristagency.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.ToursSubComponent
import com.example.touristagency.databinding.FragmentToursMainBinding
import com.example.touristagency.mvp.presenter.ToursMainPresenter
import com.example.touristagency.mvp.view.ToursView
import com.example.touristagency.ui.activity.BackButtonListener
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ToursMainFragment : MvpAppCompatFragment(), ToursView, BackButtonListener {
    private var _binding: FragmentToursMainBinding? = null
    private val binding get() = _binding!!

    private var toursSubComponent: ToursSubComponent? = null

    private var isToursButtonActive = true

    val presenter: ToursMainPresenter by moxyPresenter {
        toursSubComponent = App.instance.initUserSubComponent()

        ToursMainPresenter().apply {
            toursSubComponent?.inject(this)
        }
    }

//    var adapter: UsersRVAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
    }


    private fun switchStateButtonsToursAndHotels(fromTours: Boolean) { // boolean это проверка на какую кнопку вызывается метод, чтобы не было повторных нажатий на одну и ту же кнопку
       if (fromTours != isToursButtonActive) { // если клик не по той же кнопке
           if (!isToursButtonActive) {
               with(binding.hotelsButton) {
                   setBackgroundColor(resources.getColor(R.color.color_for_background, null))
                   setTextColor(resources.getColor(R.color.inactive_text_color, null)) // todo сделать селектор для неактивного элемента
                   icon.setTint(resources.getColor(R.color.inactive_text_color, null))
               }
               with(binding.toursButton) {
                   setBackgroundColor(resources.getColor(R.color.white, null))
                   setTextColor(resources.getColor(R.color.text_color_tours_button, null)) // todo сделать селектор для неактивного элемента
                   icon.setTint(resources.getColor(R.color.text_color_tours_button, null))
               }
           } else {
               with(binding.toursButton) {
                   setBackgroundColor(resources.getColor(R.color.color_for_background, null))
                   setTextColor(resources.getColor(R.color.inactive_text_color, null)) // todo сделать селектор для неактивного элемента
                   icon.setTint(resources.getColor(R.color.inactive_text_color, null))
               }
               with(binding.hotelsButton) {
                   setBackgroundColor(resources.getColor(R.color.white, null))
                   setTextColor(resources.getColor(R.color.text_color_tours_button, null)) // todo сделать селектор для неактивного элемента
                   icon.setTint(resources.getColor(R.color.text_color_tours_button, null))
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