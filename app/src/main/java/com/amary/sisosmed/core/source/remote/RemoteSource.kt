package com.amary.sisosmed.core.source.remote

import com.amary.sisosmed.base.BaseRemoteSource
import com.amary.sisosmed.core.source.remote.network.ApiResult
import com.amary.sisosmed.core.source.remote.network.ktor.KtorService
import com.amary.sisosmed.core.source.remote.network.retrofit.RetrofitService
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
    private val retrofitService: RetrofitService,
    private val ktorService: KtorService,
    dispatcher: CoroutineDispatcher
) : BaseRemoteSource(dispatcher) {
    suspend fun register(name: String, email: String, password: String) : Flow<ApiResult<MessageResponse>> =
        getResult({retrofitService.register(name, email, password)}, {ktorService.register(name, email, password)})

    suspend fun login(email: String, password: String): Flow<ApiResult<ApiResponse<LoginResponse>>> =
        getResult( { retrofitService.login(email, password) }, { ktorService.login(email, password) })

    suspend fun allStories(token: String, page: Int, size: Int): Flow<ApiResult<ApiResponse<List<StoryResponse>>>> =
        getResult ({ retrofitService.allStories("Bearer $token", page, size) }, {ktorService.allStories("Bearer $token", page, size)})

    suspend fun post(token: String, file: File, description: String, lat: Float, lon: Float): Flow<ApiResult<MessageResponse>> {
        val partDes = description.toRequestBody("text/plain".toMediaType())
        val partLat = lat.toString().toRequestBody("text/plain".toMediaType())
        val partLon = lon.toString().toRequestBody("text/plain".toMediaType())
        val reqFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val partImage = MultipartBody.Part.createFormData("photo", file.name, reqFile)
        return getResult({ retrofitService.post("Bearer $token", partImage, partDes, partLat, partLon) }, {ktorService.post("Bearer $token", file, description, lat.toString(), lon.toString())})
    }
}