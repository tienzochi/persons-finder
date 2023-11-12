package com.persons.finder.dto

import com.persons.finder.data.Person
import com.persons.finder.data.mapper.Mapper

data class GetPersonByIdResponseDto(
    val id: Long,
    val name: String,
)

class GetPersonByIdMapperImpl: Mapper<Person, GetPersonByIdResponseDto> {
    override fun toDto(entity: Person): GetPersonByIdResponseDto {
        return GetPersonByIdResponseDto(
            entity.id,
            entity.name,
        )
    }

    override fun toEntity(dto: GetPersonByIdResponseDto): Person {
        return Person(
            dto.id,
            dto.name,
        )
    }
}