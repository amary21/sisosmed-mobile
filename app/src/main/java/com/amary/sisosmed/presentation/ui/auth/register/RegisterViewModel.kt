package com.amary.sisosmed.presentation.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase

class RegisterViewModel(private val useCase: UseCase) : ViewModel() {
    fun register(name: String, email: String, password: String) = useCase.register(name, email, password).asLiveData()
}