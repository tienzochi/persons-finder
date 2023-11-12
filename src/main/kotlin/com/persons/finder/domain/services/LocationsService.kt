package com.persons.finder.domain.services

import com.persons.finder.data.Location
import com.persons.finder.dto.SearchResponseDto
import com.persons.finder.dto.input.SearchPeopleInputDto
import com.persons.finder.dto.input.UpdateLocationInputDto

interface LocationsService {
    fun addLocation(id: Long, location: UpdateLocationInputDto)
    fun findAround(id: Long, data: SearchPeopleInputDto) : SearchResponseDto
    fun getLocation(id: Long): Location
}