package com.amary.sisosmed.core.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amary.sisosmed.core.source.local.entity.StoryEntity

@Database(entities = [StoryEntity::class], version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {
    abstract fun dao(): Dao
}