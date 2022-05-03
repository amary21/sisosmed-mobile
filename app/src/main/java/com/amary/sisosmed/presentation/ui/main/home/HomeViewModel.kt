package com.amary.sisosmed.presentation.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.cachedIn
import com.amary.sisosmed.domain.usecase.UseCase

class HomeViewModel(private val useCase: UseCase) : ViewModel() {
    val userName = useCase.getUserName().asLiveData()
    val logout = useCase.clearAuth().asLiveData()
    fun allStories() = useCase.allStories().cachedIn(viewModelScope).asLiveData()
    fun pagerResource(combinedLoadStates: CombinedLoadStates) = useCase.pagerResource(combinedLoadStates).asLiveData()

}