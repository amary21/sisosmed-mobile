package com.amary.sisosmed.presentation.ui.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase

class SettingViewModel(useCase: UseCase) : ViewModel() {

    val logout = useCase.clearAuth().asLiveData()
}