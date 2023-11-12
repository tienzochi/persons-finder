package com.persons.finder.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GetLocationResponseDto(
    val referenceId: Long,
    val latitude: Double?,
    val longitude: Double?
)