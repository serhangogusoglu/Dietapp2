package com.example.diet_app.CongratFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CongratViewModel : ViewModel() {

    private val _navigateToHome = MutableLiveData<Boolean>(false)
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    fun onCompleteClicked() {
        _navigateToHome.value = true
    }

    fun navigationComplete() {
        _navigateToHome.value = false
    }
}