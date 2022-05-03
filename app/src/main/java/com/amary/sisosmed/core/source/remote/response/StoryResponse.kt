package com.amary.sisosmed.core.source.remote.response

import com.amary.sisosmed.constant.EmptyValue
import com.amary.sisosmed.domain.model.Story
import com.google.gson.annotations.SerializedName

data class StoryResponse(
	@SerializedName("photoUrl") val photoUrl: String = EmptyValue.STRING,
	@SerializedName("createdAt") val createdAt: String = EmptyValue.STRING,
	@SerializedName("name") val name: String = EmptyValue.STRING,
	@SerializedName("description") val description: String = EmptyValue.STRING,
	@SerializedName("lon") val lon: Double = EmptyValue.DOUBLE,
	@SerializedName("id")	val id: String = EmptyValue.STRING,
	@SerializedName("lat") val lat: Double = EmptyValue.DOUBLE
) {
	fun mapToModel() = Story(
		photoUrl = photoUrl,
		createdAt = createdAt,
		name = name,
		description = description,
		lon = lon,
		id = id,
		lat = lat
	)
}
