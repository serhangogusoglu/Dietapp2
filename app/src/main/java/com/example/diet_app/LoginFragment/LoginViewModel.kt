package com.example.diet_app.LoginFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _errorMassage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMassage

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> = _navigateBack

    fun onLoginClicked() {
        val currenEmail = email.value
        val currentPassword = password.value

        if(currenEmail.isNullOrEmpty() || currentPassword.isNullOrEmpty()) {
            _errorMassage.value = "Lütfen e-posta ve şifrenizi girin."
            _loginSuccess.value = false
            return
        }

        if(currenEmail == "test@gmail.com" && currentPassword == "123456") {
            _loginSuccess.value = true
        } else {
            _errorMassage.value = "Geçersiz e-posta veya şifre"
            _loginSuccess.value = false
        }
    }

    fun onBackClicked() {
        _navigateBack.value = true
    }

    fun operationComplete(){
        _loginSuccess.value = false
        _navigateBack.value = false
        _errorMassage.value = null
    }
}