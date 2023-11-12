package com.persons.finder.domain.services

import com.persons.finder.custom_exceptions.PersonNotFoundException
import com.persons.finder.data.Location
import com.persons.finder.data.Person
import com.persons.finder.data.repository.LocationRepository
import com.persons.finder.data.repository.PersonRepository
import com.persons.finder.dto.CreatePersonResponseDto
import com.persons.finder.dto.GetPersonByIdResponseDto
import com.persons.finder.dto.GetPersonByIdMapperImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonsServiceImpl @Autowired()
    constructor(
        private val personRepository: PersonRepository,
        private val locationRepository: LocationRepository
    )
: PersonsService {
    fun getByMultipleId(ids: List<Long>): List<Person> {
        return personRepository.findAllById(ids)
    }

    fun getOneById(id: Long): Person {
        return personRepository.findById(id)
            .orElseThrow {
                PersonNotFoundException("person with id=$id not found")
            }
    }

    fun populateDependentModels(id: Long) {
        val loc = Location(id, null, null)
        locationRepository.save(loc)
    }

    override fun getAll(): List<GetPersonByIdResponseDto> {
        val getPersonByIdMapperImpl = GetPersonByIdMapperImpl()
        return personRepository.findAll().map { getPersonByIdMapperImpl.toDto(it) }
    }

    override fun getById(idList: List<Long>): List<GetPersonByIdResponseDto> {
        val getPersonByIdMapperImpl = GetPersonByIdMapperImpl()
        return if (idList.count() > 1) {
            val res = getByMultipleId(idList)
            res.map{ getPersonByIdMapperImpl.toDto(it) }
        } else {
            listOf(getPersonByIdMapperImpl.toDto(getOneById(idList[0])))
        }
    }

    override fun save(person: Person): CreatePersonResponseDto {
        val personResponse = personRepository.save(person)
        if (!locationRepository.existsById(personResponse.id)) {
            populateDependentModels(personResponse.id)
        }

        return CreatePersonResponseDto(personResponse.id)
    }

}