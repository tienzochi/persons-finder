package com.persons.finder.dto

import com.persons.finder.data.Location
import com.persons.finder.dto.input.UpdateLocationInputDto
import org.springframework.stereotype.Component

@Component
class UpdateLocationMapperImpl: Mapper<Location, UpdateLocationInputDto> {

    override fun toDto(entity: Location): UpdateLocationInputDto {
        TODO("Not yet implemented")
    }
    override fun toEntity(dto: UpdateLocationInputDto): Location {
        return Location(
            dto.id,
            dto.latitude,
            dto.longitude
        )
    }
}