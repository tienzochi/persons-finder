package com.persons.finder.domain.services

import com.persons.finder.data.Person
import com.persons.finder.dto.CreatePersonResponseDto
import com.persons.finder.dto.GetPersonByIdResponseDto

interface PersonsService {

    fun getAll(): List<GetPersonByIdResponseDto>
    fun getById(id: List<Long>): List<GetPersonByIdResponseDto>
    fun save(person: Person): CreatePersonResponseDto
}