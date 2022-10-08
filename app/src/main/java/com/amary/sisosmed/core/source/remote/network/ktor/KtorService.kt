package com.amary.sisosmed.core.source.remote.network.ktor

import com.amary.sisosmed.core.source.remote.response.ApiResponse
import com.amary.sisosmed.core.source.remote.response.LoginResponse
import com.amary.sisosmed.core.source.remote.response.MessageResponse
import com.amary.sisosmed.core.source.remote.response.StoryResponse
import java.io.File

interface KtorService {
    suspend fun register(name: String, email: String, password: String, ): MessageResponse

    suspend fun login(email: String, password: String): ApiResponse<LoginResponse>

    suspend fun allStories(authorization: String, page: Int, size: Int, location: Int = 1): ApiResponse<List<StoryResponse>>

    suspend fun post(authorization: String, file: File, description: String, lat: String, lon: String): MessageResponse
}