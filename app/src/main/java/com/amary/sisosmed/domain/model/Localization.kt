package com.amary.sisosmed.domain.model

import com.amary.sisosmed.constant.EmptyValue

data class Localization(
    val name: String = EmptyValue.STRING,
    val code: String = EmptyValue.STRING
)
