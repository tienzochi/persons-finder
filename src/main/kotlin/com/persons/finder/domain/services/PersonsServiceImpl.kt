package com.persons.finder.domain.services

import com.persons.finder.custom_exceptions.PersonNotFoundException
import com.persons.finder.data.Location
import com.persons.finder.data.Person
import com.persons.finder.data.repository.LocationRepository
import com.persons.finder.data.repository.PersonRepository
import com.persons.finder.dto.CreatePersonResponseDto
import com.persons.finder.dto.GetPersonByIdResponseDto
import com.persons.finder.dto.PersonMapperImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonsServiceImpl @Autowired()
    constructor(
        private val personRepository: PersonRepository,
        private val locationRepository: LocationRepository,
        private val personMapperImpl: PersonMapperImpl
    )
: PersonsService {
    private fun getByMultipleId(ids: List<Long>): List<Person> {
        return personRepository.findAllById(ids)
    }

    private fun getOneById(id: Long): Person {
        return personRepository.findById(id)
            .orElseThrow {
                PersonNotFoundException("person with id=$id not found")
            }
    }

    override fun getById(idList: List<Long>): List<GetPersonByIdResponseDto> {
        return if (idList.count() > 1) {
            val res = getByMultipleId(idList)
            res.map{ personMapperImpl.toDto(it) }
        } else {
            listOf(personMapperImpl.toDto(getOneById(idList[0])))
        }
    }

    override fun save(person: Person): CreatePersonResponseDto {
        val personResponse = personRepository.save(person)
        val loc = Location(personResponse.id, null, null)
        locationRepository.save(loc)

        return CreatePersonResponseDto(personResponse.id)
    }

}