package com.amary.sisosmed.core.source.remote

import com.amary.sisosmed.base.BaseRemoteSource
import com.amary.sisosmed.core.source.remote.network.ApiResult
import com.amary.sisosmed.core.source.remote.network.ApiService
import com.amary.sisosmed.core.source.remote.response.ApiResponse
import com.amary.sisosmed.core.source.remote.response.LoginResponse
import com.amary.sisosmed.core.source.remote.response.MessageResponse
import com.amary.sisosmed.core.source.remote.response.StoryResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

class RemoteSource(
    private val apiService: ApiService,
    dispatcher: CoroutineDispatcher
) : BaseRemoteSource(dispatcher) {
    suspend fun register(name: String, email: String, password: String) : Flow<ApiResult<MessageResponse>> =
        getResult { apiService.register(name, email, password) }

    suspend fun login(email: String, password: String): Flow<ApiResult<ApiResponse<LoginResponse>>> =
        getResult { apiService.login(email, password) }

    suspend fun allStories(token: String, page: Int, size: Int, location: Int): Flow<ApiResult<ApiResponse<List<StoryResponse>>>> =
        getResult { apiService.allStories("Bearer $token", page, size, location) }
}