package com.amary.sisosmed.core.source.remote.response

import com.amary.sisosmed.constant.EmptyValue
import com.amary.sisosmed.domain.model.Message
import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("error") val error: Boolean = EmptyValue.BOOLEAN,
    @SerializedName("message") val message: String = EmptyValue.STRING,
) {
    fun mapToModel() = Message(
        error = error,
        message = message
    )
}
