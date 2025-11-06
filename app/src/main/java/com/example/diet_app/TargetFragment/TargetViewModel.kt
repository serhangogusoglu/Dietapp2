package com.example.diet_app.TargetFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
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

    val isNextButtonEnabled: LiveData<Boolean> = targetWeight.map { target ->
        target.isNotBlank() && durationDays.value?.isNotBlank() ?: false
    }

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    init {
        calculateHealthyWeightRange()
        generateSuggestion()
    }

    // Hesaplamalar

    private fun calculateHealthyWeightRange() {
        val heightCm = initialHeight.toFloatOrNull() ?: 170f
        val hInMeters = heightCm / 100f

        // sağlıklı bmi aralığı 18.5 - 24.9
        val minHealthyWeight = 18.5f * hInMeters.pow(2)
        val maxHealthyWeight = 24.9f * hInMeters.pow(2)

        _healthyRangeText.value = String.format("%.1f - %.1f kg", minHealthyWeight, maxHealthyWeight)
    }

    private fun generateSuggestion() {
        val currenctWeight = initialWeight.toFloatOrNull() ?: 70f
        val heightCm = initialHeight.toFloatOrNull() ?: 170f
        val hInMeters = heightCm / 100f

        // Kilo verme önerisi, hedef BMI'yi 22.0 kabul edelim
        val targetBmiWeight = 22.0f * hInMeters.pow(2)
        val weightToLose = currenctWeight - targetBmiWeight

        // Fazla kilolu değilse hedef korumadır, burada sadece kilo verme senaryosunu ele alalım.
        if(weightToLose > 0) {
            // Öneri süresi: Yaklaşık haftada 0.5 kg (örnek), 0.5 * 7 = 3.5 kg/ay
            // Toplam ay = weightToLose / 2
            val suggestedDays = (weightToLose / 1.5f) * 7

            _suggestionText.value = "To loose ${String.format("%.1f", weightToLose)} kg in ${suggestedDays.toInt()} days."
        } else {
            // Kullanıcı normal kiloda veya zayıfsa, hedefi kilo korumadır.
            _suggestionText.value = "Your weight is healthy. You can set a maintenance goal."
        }
    }

    fun onUnitChanged(unit:String) {
        _weightUnit.value = unit
        // NOT: Kilo birimi değiştiğinde hedef kilonun dönüştürülmesi gerekir. Bu adım şimdilik atlanmıştır.
    }

    fun onNextClicked() {
        if(isNextButtonEnabled.value == true) {
            // hedef kilonun ve sürenin islenmesi
            Log.d("TargetViewModel", "Hedef Kilo: ${targetWeight.value} ${weightUnit.value}, Süre: ${durationDays.value} gün")

            _navigateToHome.value = true
        }
    }

    fun navigationComplete() {
        _navigateToHome.value = false
    }
}