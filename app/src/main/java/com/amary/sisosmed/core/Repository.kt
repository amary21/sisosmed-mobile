package com.amary.sisosmed.core

import com.amary.sisosmed.base.BaseRepository
import com.amary.sisosmed.core.source.remote.RemoteSource
import com.amary.sisosmed.core.source.remote.network.ApiResult
import com.amary.sisosmed.core.source.remote.response.ApiResponse
import com.amary.sisosmed.core.source.remote.response.LoginResponse
import com.amary.sisosmed.core.source.remote.response.MessageResponse
import com.amary.sisosmed.core.source.remote.response.StoryResponse
import com.amary.sisosmed.core.source.session.PrefDataStore
import com.amary.sisosmed.domain.model.Login
import com.amary.sisosmed.domain.model.Message
import com.amary.sisosmed.domain.model.Story
import com.amary.sisosmed.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class Repository(
    private val remoteSource: RemoteSource,
    private val prefDataStore: PrefDataStore
) : IRepository {

    override fun register(name: String, email: String, password: String): Flow<Resource<Message>> =
        object : BaseRepository<Message, MessageResponse>(){
            override suspend fun createCall(): Flow<ApiResult<MessageResponse>>  =
                remoteSource.register(name, email, password)

            override fun mapData(data: MessageResponse): Flow<Message>  =
                flow { emit(data.mapToModel()) }
        }.asFlow()

    override fun login(email: String, password: String): Flow<Resource<Login>> =
        object : BaseRepository<Login, ApiResponse<LoginResponse>>(){
            override suspend fun createCall(): Flow<ApiResult<ApiResponse<LoginResponse>>> =
                remoteSource.login(email, password)

            override fun mapData(data: ApiResponse<LoginResponse>): Flow<Login> =
                flow {
                    prefDataStore.setToken(data.data.token)
                    prefDataStore.setName(data.data.name)
                    emit(data.data.mapToModel())
                }

        }.asFlow()

    override fun allStories(page: Int, size: Int, location: Int): Flow<Resource<List<Story>>> =
        object : BaseRepository<List<Story>, ApiResponse<List<StoryResponse>>>(){
            override suspend fun createCall(): Flow<ApiResult<ApiResponse<List<StoryResponse>>>> =
                remoteSource.allStories(prefDataStore.getToken.first(), page, size, location)

            override fun mapData(data: ApiResponse<List<StoryResponse>>): Flow<List<Story>> =
                flow { emit(data.data.map { it.mapToModel() }) }

        }.asFlow()
}