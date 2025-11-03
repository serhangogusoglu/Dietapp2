package com.example.diet_app.ui.info_second

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MediatorLiveData
import com.example.diet_app.R // R.drawable referansları için

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

    fun onNextClicked() {
        if (isNextButtonEnabled.value != true) {
            _errorMessage.value = "Lütfen tüm zorunlu alanları doldurun."
            return
        }

        // 🚨 Gerçek uygulamada: Tüm veriler (Step 1 + Step 2) toplanır, işlenir ve kaydedilir.

        _errorMessage.value = null
        _navigateToHome.value = true
    }

    fun navigationComplete() {
        _navigateToHome.value = false
    }

    fun onHeightUnitChanged(unit: String) {
        heightUnit.value = unit
    }

    fun onWeightUnitChanged(unit: String) {
        weightUnit.value = unit
    }
}