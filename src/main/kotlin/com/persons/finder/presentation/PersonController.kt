package com.persons.finder.presentation

import com.persons.finder.data.Person
import com.persons.finder.domain.services.LocationsServiceImpl
import com.persons.finder.domain.services.PersonsServiceImpl
import com.persons.finder.dto.CreatePersonResponseDto
import com.persons.finder.dto.GetPersonByIdResponseDto
import com.persons.finder.dto.SearchResponseDto
import com.persons.finder.dto.input.SearchPeopleInputDto
import com.persons.finder.dto.input.UpdateLocationInputDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/persons")
class PersonController @Autowired constructor(
    private val personsServiceImpl: PersonsServiceImpl,
    private val locationsServiceImpl: LocationsServiceImpl
) {
    /*
        TODO PUT API to update/create someone's location using latitude and longitude
        (JSON) Body
     */
    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateLocation(@RequestBody body: UpdateLocationInputDto) {
        locationsServiceImpl.addLocation(body)
    }


    /*
        TODO POST API to create a 'person'
        (JSON) Body and return the id of the created entity
    */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPerson(@RequestBody body: Person): CreatePersonResponseDto {
        return personsServiceImpl.save(body)
    }

    /*
        TODO GET API to retrieve people around query location with a radius in KM, Use query param for radius.
        TODO API just return a list of persons ids (JSON)
        // Example
        // John wants to know who is around his location within a radius of 10km
        // API would be called using John's id and a radius 10km
     */
    @GetMapping("/search")
    fun searchByRadius(
        @RequestParam latitude: String,
        @RequestParam longitude: String,
        @RequestParam radiusInKm: String,
    ): SearchResponseDto {
        return locationsServiceImpl.findAround(
            SearchPeopleInputDto(
                latitude.toDouble(),
                longitude.toDouble(),
                radiusInKm.toInt()
            )
        )
    }

    /*
        TODO GET API to retrieve a person or persons name using their ids
        // Example
        // John has the list of people around them, now they need to retrieve everybody's names to display in the app
        // API would be called using person or persons ids
     */
    @GetMapping("")
    fun getPersonById(@RequestParam id: String): List<GetPersonByIdResponseDto> {
        val idList = id.split(',')
        val convertedIdList = idList.map { it.toLong() }
        return personsServiceImpl.getById(convertedIdList)
    }
}