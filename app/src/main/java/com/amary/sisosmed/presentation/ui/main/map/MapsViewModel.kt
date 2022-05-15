package com.amary.sisosmed.presentation.ui.main.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase

class MapsViewModel(useCase: UseCase): ViewModel() {
    val getDataWithLoc = useCase.getDataWithLoc().asLiveData()
}