package com.example.diet_app.WelcomeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel: ViewModel() {

    private val _navigateToProfile = MutableLiveData<Boolean>()
    val navigateToProfile: LiveData<Boolean> = _navigateToProfile

    fun onGetStartedClicked() {
        _navigateToProfile.value = true
    }

    fun navigationComplete() {
        _navigateToProfile.value = false
    }
}