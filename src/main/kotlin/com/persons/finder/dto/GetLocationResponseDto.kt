package com.persons.finder.dto

data class GetLocationResponseDto(
    val referenceId: Long,
    val latitude: Double?,
    val longitude: Double?
)