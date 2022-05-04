package com.amary.sisosmed.presentation.ui.main.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.model.Story
import com.amary.sisosmed.domain.usecase.UseCase

class DetailViewModel(private val useCase: UseCase): ViewModel() {
    fun isFavorite(storyId: String) = useCase.isFavorite(storyId).asLiveData()
    fun setFavorite(story: Story) = useCase.setFavorite(story).asLiveData()
}