package com.amary.sisosmed.core.source.remote.network.ktor

import android.util.Log
import com.amary.sisosmed.BuildConfig
import com.amary.sisosmed.core.source.remote.response.ApiResponse
import com.amary.sisosmed.core.source.remote.response.LoginResponse
import com.amary.sisosmed.core.source.remote.response.MessageResponse
import com.amary.sisosmed.core.source.remote.response.StoryResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.File

class KtorServiceImpl(private val client: HttpClient): KtorService {
    override suspend fun register(name: String, email: String, password: String): MessageResponse {
        return client.submitForm (
            url = BuildConfig.BASE_URL + "register",
            formParameters = Parameters.build {
                append("name", name)
                append("email", email)
                append("password", password)
            }
        ).body()
    }

    override suspend fun login(email: String, password: String): ApiResponse<LoginResponse> {
        return client.submitForm(
            url = BuildConfig.BASE_URL + "login",
            formParameters = Parameters.build {
                append("email", email)
                append("password", password)
            }
        ).body()
    }

    override suspend fun allStories(
        authorization: String,
        page: Int,
        size: Int,
        location: Int
    ): ApiResponse<List<StoryResponse>> {
        return client.get(BuildConfig.BASE_URL + "stories") {
            headers { append(HttpHeaders.Authorization, authorization) }
            url {
                parameters.append("page", page.toString())
                parameters.append("size", size.toString())
                parameters.append("location", location.toString())
            }
        }.body()
    }

    override suspend fun post(
        authorization: String,
        file: File,
        description: String,
        lat: String,
        lon: String
    ): MessageResponse {
        return client.post(BuildConfig.BASE_URL + "stories"){
            headers { append(HttpHeaders.Authorization, authorization) }
            setBody(MultiPartFormDataContent(
                formData {
                    append("lat", lat)
                    append("lon", lon)
                    append("description", description)
                    append("file", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                    })
                },
            )
            )
            onUpload { bytesSentTotal, contentLength ->
                Log.e("post", "Sent $bytesSentTotal bytes from $contentLength")
            }
        }.body()
    }
}