package com.amary.sisosmed.domain.usecase

import androidx.paging.CombinedLoadStates
import com.amary.sisosmed.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow

class Interact(private val iRepository: IRepository): UseCase {
    override fun register(name: String, email: String, password: String) = iRepository.register(name, email, password)
    override fun login(email: String, password: String) = iRepository.login(email, password)
    override fun allStories() = iRepository.allStories()
    override fun pagerResource(loadState: CombinedLoadStates) = iRepository.pagerResource(loadState)
    override fun checkAuth() = iRepository.checkAuth()
    override fun clearAuth() = iRepository.clearAuth()
    override fun getUserName() = iRepository.getUserName()
}