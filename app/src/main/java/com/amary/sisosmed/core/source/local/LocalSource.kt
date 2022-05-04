package com.amary.sisosmed.core.source.local

import com.amary.sisosmed.core.source.local.entity.StoryEntity
import com.amary.sisosmed.core.source.local.room.Dao

class LocalSource(private val dao: Dao) {
    fun allFavoriteStories() = dao.allFavoriteStories()

    fun isFavorite(storyId: String) = dao.isFavorite(storyId)

    suspend fun setFavorite(storyEntity: StoryEntity) = dao.setFavorite(storyEntity)

    suspend fun unSetFavorite(storyId: String) = dao.unSetFavorite(storyId)
}