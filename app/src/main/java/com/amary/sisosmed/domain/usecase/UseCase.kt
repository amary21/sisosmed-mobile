package com.amary.sisosmed.domain.usecase

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.domain.model.Localization
import com.amary.sisosmed.domain.model.Login
import com.amary.sisosmed.domain.model.Message
import com.amary.sisosmed.domain.model.Story
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UseCase {
    fun register(name: String, email: String, password: String): Flow<Resource<Message>>
    fun login(email: String, password: String): Flow<Resource<Login>>
    fun allStories(): Flow<PagingData<Story>>
    fun pagerResource(loadState: CombinedLoadStates) : Flow<Resource<Unit>>
    fun checkAuth(): Flow<Resource<Boolean>>
    fun clearAuth(): Flow<Boolean>
    fun getUserName(): Flow<String>
    fun allFavoriteStories(): Flow<List<Story>>
    fun isFavorite(storyId: String): Flow<Boolean>
    fun setFavorite(story: Story): Flow<Boolean>
    fun post(file: File, description: String): Flow<Resource<Message>>
    fun allLocalization(): Flow<List<Localization>>
    fun setLocal(local: String): Flow<String>
    fun getLocal(): Flow<String>
    fun getDataWithLoc(): Flow<Resource<List<Story>>>
}