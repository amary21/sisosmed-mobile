package com.amary.sisosmed.domain.model

import com.amary.sisosmed.constant.EmptyValue

data class Message(
    val error: Boolean = EmptyValue.BOOLEAN,
    val message: String = EmptyValue.STRING
)
