package com.amary.sisosmed.domain.model

import com.amary.sisosmed.constant.EmptyValue

data class Story(
    val photoUrl: String = EmptyValue.STRING,
    val createdAt: String = EmptyValue.STRING,
    val name: String = EmptyValue.STRING,
    val description: String = EmptyValue.STRING,
    val lon: Double = EmptyValue.DOUBLE,
    val id: String = EmptyValue.STRING,
    val lat: Double = EmptyValue.DOUBLE
)