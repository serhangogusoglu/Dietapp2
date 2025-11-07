package com.example.diet_app.CalorieResultFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.math.roundToInt

class CalorieResultViewModelFactory(
    private val height: String,
    private val weight: String,
    private val age: Int,
    private val gender: String,
    private val activityLevel: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalorieResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalorieResultViewModel(height, weight, age, gender, activityLevel) as T
        }
        throw IllegalArgumentException("Unkown Viewmodel class")
    }
}

class CalorieResultViewModel (

    private val initialHeight: String,
    private val initialWeight: String,
    private val age: Int,
    private val gender: String,
    private val activityLevel: String
) : ViewModel() {

    private val _maintenanceKcal = MutableLiveData<Int>()
    val maintenanceKcal: LiveData<Int> = _maintenanceKcal

    init {
        calculateTDEE()
    }

    private fun getActivityMultiplier(level: String) : Float {
        return when(level.lowercase()) {
            "hareketsiz", "sedanter" -> 1.2f
            "az aktif", "hafif egzersiz", "light exercise" -> 1.375f
            "orta aktif", "orta egzersiz", "moderate exercise" -> 1.55f
            "çok aktif", "yoğun egzersiz", "heavy exercise" -> 1.725f
            "aşırı aktif", "yoğun spor/i̇ş", "extreme exercise" -> 1.9f
            else -> 1.55f
        }
    }

    private fun calculateTDEE() {
        val kilo = initialWeight.toFloatOrNull() ?: 70f
        val boy = initialHeight.toFloatOrNull() ?: 170f
        val aktiviteCarpani = getActivityMultiplier(activityLevel)

        val bmr: Float

        if(gender.lowercase() == "male" || gender.lowercase() == "erkek") {
            bmr = ((10 * kilo) + (6.25 * boy) - (5 * age) + 5).toFloat()
        } else {
            bmr = ((10 * kilo) + (6.25 * boy) - (5* age) - 161).toFloat()
        }

        val tdee = bmr * aktiviteCarpani
        _maintenanceKcal.value = tdee.roundToInt()
    }
}