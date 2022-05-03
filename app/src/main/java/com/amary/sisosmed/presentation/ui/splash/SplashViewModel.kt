package com.amary.sisosmed.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase

class SplashViewModel(useCase: UseCase) : ViewModel() {
    val checkAuthToken = useCase.allStories(1, 1, 0).asLiveData()
}