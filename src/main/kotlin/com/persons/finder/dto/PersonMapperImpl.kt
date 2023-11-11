package com.persons.finder.dto

import com.persons.finder.data.Person
import org.springframework.stereotype.Component

@Component
class PersonMapperImpl: Mapper<Person, GetPersonByIdResponseDto> {
    override fun toDto(entity: Person): GetPersonByIdResponseDto {
        return GetPersonByIdResponseDto(
            entity.id,
            entity.name
        )
    }

    override fun toEntity(dto: GetPersonByIdResponseDto): Person {
        return Person(
            dto.id,
            dto.name
        )
    }
}