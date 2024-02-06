package com.example.touristagency.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.util.Calendar

@StateStrategyType(AddToEndSingleStrategy::class)
interface HotelsView : MvpView {
    fun init()
    fun updateList()
    fun release()

    fun initBottomNavigationMenu()

    fun initCurrentCurrency(currentCurrency: String)
    fun setCitiesList(cities: MutableList<String>)
//    fun getCitiesArrayFromResourses()
    fun setMinimumNumberNights(minimumNumberOfNights: Int)
    fun setMaximumNumberNights(maximumNumberOfNights: Int)
    fun setMinimumNumberPeoples(minimumNumberOfPeople: Int)
    fun setMaximumNumberPeoples(maximumNumberOfPeople: Int)

    fun initSortingMenu(sortingStrings: List<String>)
    fun setTextSortingButton(text: String)
    fun initCityDialog()
    fun initFiltersDialog()
    fun updateCityButton(cityName: String, nightsNumber: String)
    fun initCityAndNightsButton()
    fun showCityDialog()
    fun initSortingButton()
    fun initFiltersButton()
    fun showFiltersDialog()
    fun cancelFiltersDialog()

    fun setValueToPriceNumberFrom(text: String)
    fun setValueToPriceNumberTo(text: String)
    fun setValueToStarsNumberFrom(text: String)
    fun setValueToStarsNumberTo(text: String)

    fun initMinDate(calendar: Calendar, minYear: Int, minMonth: Int, minDay: Int)
    fun setValuesFiltersDialog(
        priceNumberFromValue: String,
        priceNumberToValue: String,
        starsNumberFromValue: String,
        starsNumberToValue: String,
        foodTypesRadioGroupCheckedId: String,
        foodSystemsRadioGroupCheckedId: String,
        infrastructureCheckBox1IsChecked: Boolean,
        infrastructureCheckBox2IsChecked: Boolean,
        infrastructureCheckBox3IsChecked: Boolean,
        infrastructureCheckBox4IsChecked: Boolean,
        infrastructureCheckBox5IsChecked: Boolean,
        infrastructureCheckBox6IsChecked: Boolean,
        infrastructureCheckBox7IsChecked: Boolean
    )

    fun cancelCityDialog()
    fun dismissCityDialog()
    fun initFindButtonCityDialog()
    fun setValuesCityDialog(
        cityName: String,
        date: String,
        nightsNumber: String,
        peoplesNumber: String
    )

    fun updateImage(position: Int)

    fun showSnackbar(text: String)
    fun showSnackBarAboutValueCityDialog(text: String)

    fun setVisibilityNotFoundLayout(visibility: Boolean)
    fun setVisibilityRecyclerView(visibility: Boolean)

    fun stopRefreshing()

}