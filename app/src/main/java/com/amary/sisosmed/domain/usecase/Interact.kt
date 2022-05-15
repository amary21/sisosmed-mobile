package com.amary.sisosmed.domain.usecase

import androidx.paging.CombinedLoadStates
import com.amary.sisosmed.domain.model.Story
import com.amary.sisosmed.domain.repository.IRepository
import java.io.File

class Interact(private val iRepository: IRepository): UseCase {
    override fun register(name: String, email: String, password: String) = iRepository.register(name, email, password)
    override fun login(email: String, password: String) = iRepository.login(email, password)
    override fun allStories() = iRepository.allStories()
    override fun pagerResource(loadState: CombinedLoadStates) = iRepository.pagerResource(loadState)
    override fun checkAuth() = iRepository.checkAuth()
    override fun clearAuth() = iRepository.clearAuth()
    override fun getUserName() = iRepository.getUserName()
    override fun allFavoriteStories() = iRepository.allFavoriteStories()
    override fun isFavorite(storyId: String) = iRepository.isFavorite(storyId)
    override fun setFavorite(story: Story) = iRepository.setFavorite(story)
    override fun post(file: File, description: String, lat: Float, lon: Float) = iRepository.post(file, description, lat, lon)
    override fun allLocalization() = iRepository.allLocalization()
    override fun setLocal(local: String) = iRepository.setLocal(local)
    override fun getLocal() = iRepository.getLocal()
    override fun getDataWithLoc() = iRepository.getDataWithLoc()
}