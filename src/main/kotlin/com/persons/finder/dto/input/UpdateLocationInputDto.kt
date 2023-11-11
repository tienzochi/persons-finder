package com.persons.finder.dto.input

data class UpdateLocationInputDto(
    val id: Long,
    val latitude: Double?,
    val longitude: Double?,
)
