package com.amary.sisosmed.core.source.local.room

import androidx.room.*
import androidx.room.Dao
import com.amary.sisosmed.core.source.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM story")
    fun allFavoriteStories(): Flow<List<StoryEntity>>

    @Query("SELECT EXISTS (SELECT * FROM story WHERE id=:storyId)")
    fun isFavorite(storyId: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setFavorite(storyEntity: StoryEntity)

    @Query("DELETE FROM story WHERE id=:storyId")
    suspend fun unSetFavorite(storyId: String)

}