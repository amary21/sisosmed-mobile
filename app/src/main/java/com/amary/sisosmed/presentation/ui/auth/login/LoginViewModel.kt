package com.amary.sisosmed.presentation.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase

class LoginViewModel(private val useCase: UseCase) : ViewModel() {
    fun login(email: String, password: String) = useCase.login(email, password).asLiveData()
}