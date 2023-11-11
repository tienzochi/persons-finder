package com.persons.finder.dto.input

data class SearchPeopleInputDto(
    val latitude: Double,
    val longitude: Double,
    val radiusInKm: Int = 100
)
