package com.amary.sisosmed.domain.model

import com.amary.sisosmed.constant.EmptyValue

data class Login(
    val name: String =  EmptyValue.STRING,
    val userId: String = EmptyValue.STRING,
    val token: String = EmptyValue.STRING
)
