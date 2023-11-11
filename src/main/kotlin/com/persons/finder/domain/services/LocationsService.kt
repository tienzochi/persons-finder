package com.persons.finder.domain.services

import com.persons.finder.data.Location
import com.persons.finder.dto.SearchResponseDto
import com.persons.finder.dto.input.SearchPeopleInputDto
import com.persons.finder.dto.input.UpdateLocationInputDto

interface LocationsService {
    fun addLocation(location: UpdateLocationInputDto)
    fun findAround(data: SearchPeopleInputDto) : SearchResponseDto
}