package com.example.diet_app.LoginFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diet_app.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepositroy: UserRepository): ViewModel() {
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

        viewModelScope.launch {
            val user = userRepositroy.authenticateUser(currenEmail, currentPassword)

            if(user != null) {
                _loginSuccess.value = true
            } else {
                _errorMassage.value = "Geçersiz e-posta veya şifre."
            }
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
    // ViewModel Factory için Companion Object
    companion object {
        fun Factory(userRepository: UserRepository) = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return LoginViewModel(userRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}