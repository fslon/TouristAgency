package com.example.touristagency.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.example.touristagency.App
import com.example.touristagency.R
import com.example.touristagency.dagger.subComponents.ProfileSubComponent
import com.example.touristagency.databinding.FragmentProfileBinding
import com.example.touristagency.mvp.presenter.ProfilePresenter
import com.example.touristagency.mvp.view.ProfileView
import com.example.touristagency.ui.activity.BackButtonListener
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.util.Calendar
import java.util.Date


class ProfileFragment : MvpAppCompatFragment(), ProfileView, BackButtonListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var profileSubComponent: ProfileSubComponent? = null

    val presenter: ProfilePresenter by moxyPresenter {
        profileSubComponent = App.instance.initProfileSubComponent()

        ProfilePresenter().apply {
            profileSubComponent?.inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun release() {
        profileSubComponent = null
        App.instance.releaseProfileSubComponent()
    }

    override fun initBottomNavigationMenu() {
        binding.bottomNavigation.menu.findItem(R.id.item_profile).isChecked = true
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

    override fun initDatePicker() {
        lateinit var startDatePicker: DatePickerDialog
        var startDate: Date
        val textViewButtonDate = binding.birthDateTextviewPicker
        startDatePicker = createDatePicker { selectedDate ->
            startDate = selectedDate
            val startDateStr = presenter.formatDate(startDate)
            textViewButtonDate.text = startDateStr
        }
        textViewButtonDate.setOnClickListener {
            startDatePicker.show()
        }
    }

    private fun createDatePicker(onDateSelected: (Date) -> Unit): DatePickerDialog { // создание окна выбора даты для кнопки выбора числа вылета
        val calendar = Calendar.getInstance()

        val listener = DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            onDateSelected(calendar.time)
        }
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            listener,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        return datePickerDialog
    }



    override fun showSnack(text: String) {
        Snackbar.make(binding.container, text, Snackbar.LENGTH_SHORT).show()
    }


    override fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            presenter.loginButtonOnClick(binding.loginTextfield.editText?.text.toString(), binding.passwordTextfield.editText?.text.toString())
        }
    }

    override fun initRegisterButton() {
        binding.registerButton.setOnClickListener {

            presenter.registerButtonOnClick(
                binding.loginTextfield.editText?.text.toString(), binding.passwordTextfield.editText?.text.toString(),
                binding.emailTextfield.editText?.text.toString(),
                binding.birthDateTextviewPicker.text.toString(),
                binding.nameTextfield.editText?.text.toString(),
                binding.lastNameTextfield.editText?.text.toString(),
                binding.surnameTextfield.editText?.text.toString()
            )
        }
    }


    override fun switchIsRegisteredText(isRegistered: Boolean) {
        if (isRegistered) {
            binding.accountStatusResultTextview.text = resources.getString(R.string.profile_account_status_result_textview_registered)
            binding.accountStatusResultTextview.setTextColor(
                resources.getColor(
                    R.color.profile_account_status_result_textview_registered_color,
                    null
                )
            )

        } else {
            binding.accountStatusResultTextview.text = resources.getString(R.string.profile_account_status_result_textview_not_registered)
            binding.accountStatusResultTextview.setTextColor(
                resources.getColor(
                    R.color.profile_account_status_result_textview_not_registered_color,
                    null
                )
            )
        }
    }

    override fun backPressed() = presenter.backPressed()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() = ProfileFragment()
    }


}