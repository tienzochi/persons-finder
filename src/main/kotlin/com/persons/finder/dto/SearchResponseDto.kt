package com.persons.finder.dto

data class SearchResponseDto(
    val results: List<SearchResultDto>
)

data class SearchResultDto(
    val id: Long,
    val distanceValue: Double,
    val distanceUnit: String = "KM"
)
