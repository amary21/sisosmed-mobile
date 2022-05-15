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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RemoteSource(
    private val apiService: ApiService,
    dispatcher: CoroutineDispatcher
) : BaseRemoteSource(dispatcher) {
    suspend fun register(name: String, email: String, password: String) : Flow<ApiResult<MessageResponse>> =
        getResult { apiService.register(name, email, password) }

    suspend fun login(email: String, password: String): Flow<ApiResult<ApiResponse<LoginResponse>>> =
        getResult { apiService.login(email, password) }

    suspend fun allStories(token: String, page: Int, size: Int): Flow<ApiResult<ApiResponse<List<StoryResponse>>>> =
        getResult { apiService.allStories("Bearer $token", page, size) }

    suspend fun post(token: String, file: File, description: String, lat: Float, lon: Float): Flow<ApiResult<MessageResponse>> {
        val partDes = description.toRequestBody("text/plain".toMediaType())
        val partLat = lat.toString().toRequestBody("text/plain".toMediaType())
        val partLon = lon.toString().toRequestBody("text/plain".toMediaType())
        val reqFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val partImage = MultipartBody.Part.createFormData("photo", file.name, reqFile)
        return getResult { apiService.post("Bearer $token", partImage, partDes, partLat, partLon) }
    }
}