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
import org.springframework.web.bind.annotation.PathVariable
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
    /**
     * Update/Create a person's location using latitude and longitude
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateLocation(
        @PathVariable id: String,
        @RequestBody body: UpdateLocationInputDto,
    ) {
        locationsServiceImpl.addLocation(id.toLong(), body)
    }

    /**
     * Creates a person and initialise location
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createPerson(@RequestBody body: Person): CreatePersonResponseDto {
        return personsServiceImpl.save(body)
    }

    /**
     * Returns list of persons id with distance sorted by nearest to the point
     */
    @GetMapping("/{id}/search")
    fun searchByRadius(
        @PathVariable id: String,
        @RequestParam radiusInKm: String,
    ): SearchResponseDto {
        val location = locationsServiceImpl.getLocation(id.toLong())
        return locationsServiceImpl.findAround(
            id.toLong(),
            SearchPeopleInputDto(
                location.latitude!!,
                location.longitude!!,
                radiusInKm.toInt()
            )
        )
    }

    /**
     * Returns a person or persons name using their id/s
     */
    @GetMapping()
    fun getPersonById(@RequestParam id: String): List<GetPersonByIdResponseDto> {
        val idList = id.split(',')
        val convertedIdList = idList.map { it.toLong() }
        return personsServiceImpl.getById(convertedIdList)
    }

    @PostMapping("populate-locations")
    fun populateLocations(@RequestBody locs: LocationPopulate) {
        for (i in 1..11) {
            personsServiceImpl.save(Person(i.toLong(), i.toString()))
        }

        for(loc in locs.locations) {
            locationsServiceImpl.addLocation(
                loc.id,
                UpdateLocationInputDto(loc.latitude, loc.longitude)
            )
        }
    }

}

data class LocationPopulate(
    val locations: List<PopulateLocationInputDto>
)

data class PopulateLocationInputDto(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
)