package com.amary.sisosmed.core.source.remote.response

import com.amary.sisosmed.constant.EmptyValue
import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
	@SerializedName("error") val error: Boolean = EmptyValue.BOOLEAN,
	@SerializedName("message") val message: String = EmptyValue.STRING,
	@SerializedName(value = "loginResult", alternate = ["listStory"]) var data: T
)