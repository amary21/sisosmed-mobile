package com.amary.sisosmed.domain.repository

import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.domain.model.Login
import com.amary.sisosmed.domain.model.Message
import com.amary.sisosmed.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface IRepository {

    fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<Message>>

    fun login(
        email: String,
        password: String
    ):  Flow<Resource<Login>>

    fun allStories(
        page: Int,
        size: Int,
        location: Int
    ): Flow<Resource<List<Story>>>
}