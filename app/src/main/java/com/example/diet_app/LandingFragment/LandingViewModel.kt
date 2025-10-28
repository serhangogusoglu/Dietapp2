package com.example.diet_app.LandingFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//Açılış(landing) ekranının UI mantığını ve durumunu yönetir

class LandingViewModel : ViewModel() {

    //Login ekranına gezinme olayını tetiklemek için LiveData
    private  val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> = _navigateToLogin

    // Sign Up ekranına gezinme olayını tetiklemek için LiveData
    private val _navigateToSignUp = MutableLiveData<Boolean>()
    val navigateToSignUp: LiveData<Boolean> = _navigateToSignUp

    // login butonuna tıklanıldığında çağırılır
    fun onLoginClicked() {
        _navigateToLogin.value = true
    }

    // sign up metnine tıklanıldıgında cagırılır
    fun onSignUpClicked() {
        _navigateToSignUp.value = true
    }

    // Gezinme gerçekleştikten sonra LiveData durumunu sıfırlamak için kullanılır.
    //Bu, gezinmenin sadece bir kez tetiklenmesini sağlar.

    fun navigationComplete() {
        _navigateToLogin.value = false
        _navigateToSignUp.value = false
    }
}