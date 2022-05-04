package com.amary.sisosmed.presentation.ui.main.setting.localization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase

class LocalizationViewModel(private val useCase: UseCase): ViewModel() {
    val dataLocal = useCase.allLocalization().asLiveData()
    fun setLocal(local: String) = useCase.setLocal(local).asLiveData()
}