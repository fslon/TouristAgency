package com.example.touristagency.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.util.Calendar

@StateStrategyType(AddToEndSingleStrategy::class)
interface ToursView : MvpView {
    fun init()
    fun updateList()
    fun release()

    fun initCurrentCurrency(currentCurrency: String)
    fun initSortingMenu(sortingStrings: List<String>)
    fun setTextSortingButton(text: String)
    fun initCityDialog()
    fun initFiltersDialog()
    fun updateCityButton()
    fun initCityAndNightsButton()
    fun showCityDialog()
    fun initToursButton()
    fun initHotelsButton()
    fun initSortingButton()
    fun initFiltersButton()
    fun showFiltersDialog()
    fun cancelFiltersDialog()

    fun setValueToPriceNumberFrom(text: String)
    fun setValueToPriceNumberTo(text: String)
//    fun setValueToRangeSliderPrice(value1: Float, value2: Float)
//    fun setValueToRangeSliderStars(value1: Float, value2: Float)
    fun setValueToStarsNumberFrom(text: String)
    fun setValueToStarsNumberTo(text: String)
//    fun setValueToFoodTypesRadioGroup(elementId: Int)
//    fun setValueToFoodSystemsRadioGroup(elementId: Int)
//    fun setValueToInfrastructureCheckBox1(isChecked: Boolean)
//    fun setValueToInfrastructureCheckBox2(isChecked: Boolean)
//    fun setValueToInfrastructureCheckBox3(isChecked: Boolean)
//    fun setValueToInfrastructureCheckBox4(isChecked: Boolean)
//    fun setValueToInfrastructureCheckBox5(isChecked: Boolean)
//    fun setValueToInfrastructureCheckBox6(isChecked: Boolean)
//    fun setValueToInfrastructureCheckBox7(isChecked: Boolean)


    fun switchStateButtonsToursAndHotels(fromTours: Boolean)
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

//    fun getValuesFiltersDialog(
//        priceNumberFromKey: String,
//        priceNumberToKey: String,
//        starsNumberFromKey: String,
//        starsNumberToKey: String,
//        foodTypesRadioGroupKey: String,
//        foodSystemsRadioGroupKey: String,
//        infrastructureCheckBox1Key: String,
//        infrastructureCheckBox2Key: String,
//        infrastructureCheckBox3Key: String,
//        infrastructureCheckBox4Key: String,
//        infrastructureCheckBox5Key: String,
//        infrastructureCheckBox6Key: String,
//        infrastructureCheckBox7Key: String
//    )

    fun testInitFirstRecyclerItem()
    fun testInitSecondRecyclerItem()


}