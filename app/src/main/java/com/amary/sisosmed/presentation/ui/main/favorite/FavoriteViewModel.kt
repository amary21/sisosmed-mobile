package com.amary.sisosmed.presentation.ui.main.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase

class FavoriteViewModel(useCase: UseCase) : ViewModel() {
    val userName = useCase.getUserName().asLiveData()
}