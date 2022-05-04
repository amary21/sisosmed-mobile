package com.amary.sisosmed.core.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amary.sisosmed.constant.EmptyValue
import com.amary.sisosmed.domain.model.Story

@Entity(tableName = "story")
data class StoryEntity (
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id") var id: String = EmptyValue.STRING,
    @ColumnInfo(name = "photo_url") var photoUrl: String = EmptyValue.STRING,
    @ColumnInfo(name = "created_at") var createdAt: String = EmptyValue.STRING,
    @ColumnInfo(name = "name") var name: String = EmptyValue.STRING,
    @ColumnInfo(name = "description") var description: String = EmptyValue.STRING,
    @ColumnInfo(name = "lon") var lon: Double = EmptyValue.DOUBLE,
    @ColumnInfo(name = "lat") var lat: Double = EmptyValue.DOUBLE
) {
    fun mapToModel() = Story(
        id = id,
        photoUrl = photoUrl,
        createdAt = createdAt,
        name = name,
        description = description,
        lon = lon,
        lat = lat
    )
}

