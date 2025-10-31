package com.example.diet_app.InfoFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MediatorLiveData // 👈 Yeni import

class InfoViewModel: ViewModel() {

    // --- Form Alanları ---
    val name = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val country = MutableLiveData<String>()
    val purpose = MutableLiveData<String>() // 👈 BottomSheet'ten gelen zorunlu alan

    // --- Gezinme ve Mesaj Durumları ---
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _navigateToNextStep = MutableLiveData<Boolean>()
    val navigateToNextStep: LiveData<Boolean> = _navigateToNextStep

    // --- Buton Etkinleştirme Mantığı ---

    /**
     * Tüm zorunlu alanların (name, email, country, purpose) doldurulup doldurulmadığını kontrol eder.
     */
    private val areAllRequiredFieldsValid: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        // Tüm LiveData kaynaklarını dinlemeye başla
        addSource(name) { value = checkValidity() }
        addSource(email) { value = checkValidity() }
        addSource(country) { value = checkValidity() }
        addSource(purpose) { value = checkValidity() }
    }

    /**
     * 💡 Next butonunun UI'da aktif/deaktif durumunu yönetir.
     * Bu, Fragment'ta gözlemlenecektir.
     */
    val isNextButtonEnabled: LiveData<Boolean> = areAllRequiredFieldsValid


    // --- Metotlar ---

    /**
     * Zorunlu alanların hepsinin dolu olup olmadığını kontrol eden dahili fonksiyon.
     */
    private fun checkValidity(): Boolean {
        // Not: purpose.value, BottomSheet'ten seçim yapılana kadar null veya boş olacaktır.
        return  !name.value.isNullOrBlank() &&
                !email.value.isNullOrBlank() &&
                !country.value.isNullOrBlank() &&
                !purpose.value.isNullOrBlank() // 👈 Zorunluluk artık bu kontrolle sağlanıyor
    }

    fun onNextClicked() {
        // Buton UI'da deaktif olsa da, son bir kontrol her zaman iyidir.
        if (isNextButtonEnabled.value != true) {
            _errorMessage.value = "Lütfen tüm zorunlu alanları doldurun."
            return
        }

        _errorMessage.value = null
        _navigateToNextStep.value = true
        // 🚨 Burada Room'a veya Repository'e kaydetme işlemi yapılmalıdır.
    }

    fun onBackClicked() {
        // Geri gezinme mantığı
    }

    fun navigationComplete() {
        _navigateToNextStep.value = false
    }
}