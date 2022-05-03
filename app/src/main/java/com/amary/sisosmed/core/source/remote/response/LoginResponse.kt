package com.amary.sisosmed.core.source.remote.response

import com.amary.sisosmed.constant.EmptyValue
import com.amary.sisosmed.domain.model.Login
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("name") val name: String =  EmptyValue.STRING,
    @SerializedName("userId") val userId: String = EmptyValue.STRING,
    @SerializedName("token") val token: String = EmptyValue.STRING
) {
    fun mapToModel() = Login(
        name = name,
        userId = userId,
        token = token
    )
}