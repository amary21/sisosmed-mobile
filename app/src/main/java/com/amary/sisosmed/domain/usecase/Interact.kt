package com.amary.sisosmed.domain.usecase

import com.amary.sisosmed.domain.repository.IRepository

class Interact(private val iRepository: IRepository): UseCase {
    override fun register(name: String, email: String, password: String) = iRepository.register(name, email, password)
    override fun login(email: String, password: String) = iRepository.login(email, password)
    override fun allStories(page: Int, size: Int, location: Int) = iRepository.allStories(page, size, location)
    override fun clearAuth() = iRepository.clearAuth()
}