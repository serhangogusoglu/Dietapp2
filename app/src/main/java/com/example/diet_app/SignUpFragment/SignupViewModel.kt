package com.example.diet_app.SignUpFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> = _navigateToLogin

    fun onSignupClicked(){
        val currentEmail = email.value
        val currentPassword = password.value
        val currentConfirmPassword = confirmPassword.value

        if(currentEmail.isNullOrEmpty() || currentPassword.isNullOrEmpty() || currentConfirmPassword.isNullOrEmpty()) {
            _errorMessage.value = "Lütfen tüm alanları doldurun."
            _registrationSuccess.value = false
            return
        }

        if(currentPassword != currentConfirmPassword) {
            _errorMessage.value = "Şifreler eşleşmiyor."
            _registrationSuccess.value=false
            return
        }

        // başarılı kayıt simülasyon
        _registrationSuccess.value = true
    }

    fun onBackClicked(){
        _navigateBack.value = true
    }

    fun onLoginTextClicked(){
        _navigateToLogin.value = true
    }

    fun operationComplete(){
        _registrationSuccess.value = false
        _navigateBack.value = false
        _navigateToLogin.value = false
        _errorMessage.value = null
    }
}