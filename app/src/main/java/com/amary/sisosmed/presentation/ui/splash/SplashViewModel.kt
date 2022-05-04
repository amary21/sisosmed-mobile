package com.amary.sisosmed.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase

class SplashViewModel(useCase: UseCase) : ViewModel() {
    val checkAuthToken = useCase.checkAuth().asLiveData()
    val getLocal = useCase.getLocal().asLiveData()
}