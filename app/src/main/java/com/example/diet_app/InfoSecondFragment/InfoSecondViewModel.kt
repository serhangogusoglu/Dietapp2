package com.example.diet_app.ui.info_second

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.diet_app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InfoSecondViewModel : ViewModel() {

    // --- Form Verileri ---
    val height = MutableLiveData<String>()
    val weight = MutableLiveData<String>()
    val weekMovement = MutableLiveData<String>() // Haftalık hareket
    val gender = MutableLiveData<String>()       // Cinsiyet
    val birthDate = MutableLiveData<String>()    // Doğum Tarihi

    // ✅ PROFİL RESMİ: Null ile başlatılır.
    val profileImageResId = MutableLiveData<Int?>(null)

    // --- UI/Birim Durumu ---
    val heightUnit = MutableLiveData("cm") // Varsayılan cm
    val weightUnit = MutableLiveData("kg") // Varsayılan kg

    // --- Gezinme ve Mesaj Durumu ---
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    // ✅ YENİ: BMI Hesaplama ve Gecikmeli Navigasyon
    private val _navigateToBmiResult = MutableLiveData<BmiResultData?>()
    val navigateToBmiResult: LiveData<BmiResultData?> = _navigateToBmiResult


    // --- Buton Etkinleştirme Mantığı ---
    val isNextButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(height) { value = checkValidity() }
        addSource(weight) { value = checkValidity() }
        addSource(gender) { value = checkValidity() }
        addSource(birthDate) { value = checkValidity() }
    }

    private fun checkValidity(): Boolean {
        // Height, Weight, Gender ve Birth Date zorunlu varsayılmıştır
        return !height.value.isNullOrBlank() &&
                !weight.value.isNullOrBlank() &&
                !gender.value.isNullOrBlank() &&
                !birthDate.value.isNullOrBlank()
    }

    // --- Metotlar ---

    // ✅ onNextClicked metodu güncellendi: Gecikmeli navigasyonu başlatır
    fun onNextClicked() {
        if (isNextButtonEnabled.value != true) {
            _errorMessage.value = "Lütfen tüm zorunlu alanları doldurun."
            return
        }

        _errorMessage.value = null

        // Coroutine başlat: 2 saniye gecikmeli hesaplama ve gezinme
        viewModelScope.launch {
            // 1. BMI Hesaplamasını Gerçekleştir
            val bmiResult = calculateBmi()

            // 2. Navigasyonu Başlat: İlk önce Calculation fragment'a geçişi tetikle
            _navigateToBmiResult.value = BmiResultData(
                bmi = bmiResult.bmi,
                category = bmiResult.category,
                targetFragment = TargetFragment.CALCULATION
            )

            // 3. Calculation ekranının gösterilmesi için 2 saniye bekle
            delay(2000)

            // 4. BMI Sonuç ekranına geçişi tetikle (Calculation ekranından buraya geri dönülmez)
            _navigateToBmiResult.value = BmiResultData(
                bmi = bmiResult.bmi,
                category = bmiResult.category,
                targetFragment = TargetFragment.BMI_RESULT
            )
        }
    }

    // ✅ BMI Hesaplama Metodu
    private fun calculateBmi(): BmiResultData {
        val heightValue = height.value?.toFloatOrNull() ?: return BmiResultData(0.0, "Hata", TargetFragment.BMI_RESULT)
        var weightValue = weight.value?.toFloatOrNull() ?: return BmiResultData(0.0, "Hata", TargetFragment.BMI_RESULT)

        // Girdilerin birimlere göre dönüştürülmesi
        val hInMeters = if (heightUnit.value == "cm") heightValue / 100f else heightValue * 0.3048f
        weightValue = if (weightUnit.value == "lbs") weightValue * 0.453592f else weightValue

        // BMI = Ağırlık (kg) / Boy^2 (m^2)
        val bmi = if (hInMeters > 0) weightValue / (hInMeters * hInMeters) else 0.0f
        val formattedBmi = "%.2f".format(bmi).toDouble()

        val category = when {
            formattedBmi < 18.5 -> "Underweight"
            formattedBmi < 24.9 -> "Normal weight"
            formattedBmi < 29.9 -> "Overweight"
            else -> "Obesity"
        }

        return BmiResultData(formattedBmi, category, TargetFragment.CALCULATION)
    }

    fun navigationComplete() {
        _navigateToHome.value = false
    }

    fun navigationToBmiResultComplete() {
        _navigateToBmiResult.value = null
    }

    fun onHeightUnitChanged(unit: String) {
        heightUnit.value = unit
    }

    fun onWeightUnitChanged(unit: String) {
        weightUnit.value = unit
    }
}

// ✅ Yeni Veri Sınıfları (ViewModel'in yanına eklendi)
data class BmiResultData(
    val bmi: Double,
    val category: String,
    val targetFragment: TargetFragment
)

enum class TargetFragment {
    CALCULATION, // 2 saniyelik ekran
    BMI_RESULT   // Sonuç ekranı
}