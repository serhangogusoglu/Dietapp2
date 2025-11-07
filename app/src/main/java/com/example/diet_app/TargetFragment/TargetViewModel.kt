package com.example.diet_app.TargetFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.MediatorLiveData
import kotlin.math.pow

class TargetViewModelFactory(private val height: String, private val weight: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TargetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TargetViewModel(height, weight) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TargetViewModel(private val initialHeight: String, private val initialWeight: String) : ViewModel() {

    val targetWeight = MutableLiveData<String>()
    val durationDays = MutableLiveData<String>()

    private val _weightUnit = MutableLiveData("kg")
    val weightUnit: LiveData<String> = _weightUnit

    private val _healthyRangeText = MutableLiveData<String>()
    val healthyRangeText: LiveData<String> = _healthyRangeText

    private val _suggestionText = MutableLiveData<String>()
    val suggestionText: LiveData<String> = _suggestionText

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    init {
        calculateHealthyWeightRange()
        generateSuggestion()
    }

    val isNextButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        // 1. targetWeight değiştiğinde kontrol et
        addSource(targetWeight) {
            value = isInputsValid()
        }
        // 2. durationDays değiştiğinde kontrol et
        addSource(durationDays) {
            value = isInputsValid()
        }
        // Başlangıç değerini ayarla
        value = isInputsValid()
    }

    private fun isInputsValid(): Boolean {
        // Her iki alanın da boş olup olmadığını kontrol eder
        val targetValid = targetWeight.value?.isNotBlank() ?: false
        val durationValid = durationDays.value?.isNotBlank() ?: false
        return targetValid && durationValid
    }

    // Hesaplamalar (Aynı Kaldı)
    private fun calculateHealthyWeightRange() {
        // ... (Kod aynı kaldı)
        val heightCm = initialHeight.toFloatOrNull() ?: 170f
        val hInMeters = heightCm / 100f
        val minHealthyWeight = 18.5f * hInMeters.pow(2)
        val maxHealthyWeight = 24.9f * hInMeters.pow(2)
        _healthyRangeText.value = String.format("%.1f - %.1f kg", minHealthyWeight, maxHealthyWeight)
    }

    private fun generateSuggestion() {
        // ... (Kod aynı kaldı)
        val currenctWeight = initialWeight.toFloatOrNull() ?: 70f
        val heightCm = initialHeight.toFloatOrNull() ?: 170f
        val hInMeters = heightCm / 100f
        val targetBmiWeight = 22.0f * hInMeters.pow(2)
        val weightToLose = currenctWeight - targetBmiWeight

        if(weightToLose > 0) {
            val suggestedDays = (weightToLose / 1.5f) * 7
            _suggestionText.value = "To loose ${String.format("%.1f", weightToLose)} kg in ${suggestedDays.toInt()} days."
        } else {
            _suggestionText.value = "Your weight is healthy. You can set a maintenance goal."
        }
    }

    fun onUnitChanged(unit:String) {
        _weightUnit.value = unit
    }

    fun onNextClicked() {
        if(isNextButtonEnabled.value == true) {
            Log.d("TargetViewModel", "Hedef Kilo: ${targetWeight.value} ${weightUnit.value}, Süre: ${durationDays.value} gün")
            _navigateToHome.value = true
        }
    }

    fun navigationComplete() {
        _navigateToHome.value = false
    }
}