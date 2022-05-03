package com.amary.sisosmed.core

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import com.amary.sisosmed.base.BasePagingSource
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
import kotlinx.coroutines.flow.map

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

    override fun allStories(): Flow<PagingData<Story>> =
        BasePagingSource.build { page ->
            remoteSource.allStories(
                prefDataStore.getToken.first(),
                page,
                5,
                0
            )
        }.flow.map { paging -> paging.map { it.mapToModel() } }

    override fun pagerResource(loadState: CombinedLoadStates): Flow<Resource<Unit>> = flow {
        when(val result = loadState.source.refresh){
            is LoadState.Loading -> emit(Resource.Loading())
            is LoadState.NotLoading -> emit(Resource.Success(Unit))
            is LoadState.Error -> {
                val error = result.error.message?.split(" - ")
                val codeResponse = error?.get(0)
                val message = error?.get(1)

                when(codeResponse?.toInt()){
                    401 -> emit(Resource.Unauthorized(message.toString()))
                    500, 502 -> emit(Resource.ServerError(message.toString()))
                    else -> emit(Resource.Failed(message.toString()))
                }
            }
        }
    }

    override fun checkAuth(): Flow<Resource<Boolean>> =
        object : BaseRepository<Boolean, ApiResponse<List<StoryResponse>>>(){
            override suspend fun createCall(): Flow<ApiResult<ApiResponse<List<StoryResponse>>>> =
                remoteSource.allStories(prefDataStore.getToken.first(), 1, 1, 0)

            override fun mapData(data: ApiResponse<List<StoryResponse>>): Flow<Boolean> = flow {
                if (data.data.isNullOrEmpty()){
                    emit(false)
                } else {
                    emit(true)
                }
            }

        }.asFlow()

    override fun clearAuth() = prefDataStore.clear()

    override fun getUserName(): Flow<String> = prefDataStore.getName
}