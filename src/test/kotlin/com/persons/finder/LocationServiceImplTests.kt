package com.persons.finder

import com.persons.finder.custom_exceptions.LocationNotFoundException
import com.persons.finder.data.Location
import com.persons.finder.data.repository.LocationRepository
import com.persons.finder.domain.services.LocationsServiceImpl
import com.persons.finder.dto.SearchResponseDto
import com.persons.finder.dto.SearchResultDto
import com.persons.finder.dto.input.SearchPeopleInputDto
import com.persons.finder.dto.input.UpdateLocationInputDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class LocationServiceImplTests {
    private val locationRepositoryMock = mock(LocationRepository::class.java)
    private val locationServiceImpl = LocationsServiceImpl(locationRepositoryMock)

    @BeforeEach
    fun beforeEach() {
        reset(locationRepositoryMock)
    }

    @Test
    fun `Should get location`() {
        val expectedLocation = Location(1, 51.0, -1.0)
        `when`(locationRepositoryMock.findById(1)).thenReturn(Optional.of(expectedLocation))
        val loc = locationServiceImpl.getLocation(1)
        assertEquals(expectedLocation, loc)
    }

    @Test
    fun `Should return LocationNotFoundException if missing location`() {
        assertThrows<LocationNotFoundException> {
            locationServiceImpl.getLocation(1)
        }
    }

    @Test
    fun `Should have called save() one time when adding location`() {
        locationServiceImpl.addLocation(1, UpdateLocationInputDto(51.0, -1.0))
        verify(locationRepositoryMock, times(1)).save(Location(1, 51.0, -1.0))
    }

    @Test
    fun `Should return empty list if all locations lat lon are null`() {
        `when`(locationRepositoryMock.findAll())
            .thenReturn(listOf(
                Location(1, null, null),
                Location(2, null, null)
            ))
        val actualLocations = locationServiceImpl.findAround(1, SearchPeopleInputDto(51.0,-1.0))
        assertEquals(SearchResponseDto(listOf()), actualLocations)
    }

    @Test
    fun `Should return sorted locations`() {
        val expectedLocation1 = SearchResultDto(2, 7.559568937053553)
        val expectedLocation2 = SearchResultDto(3, 10.604187773566299)
        val expectedLocation3 = SearchResultDto(4, 16.034729146854456)
        `when`(locationRepositoryMock.findAll())
            .thenReturn(listOf(
                Location(1, 50.966184,-0.888519),
                Location(2, 51.011136,-0.969543),
                Location(3, 51.002495,-1.028595),
                Location(4, 51.040503,-1.084900)
            ))
        val actualLocations = locationServiceImpl.findAround(1, SearchPeopleInputDto(50.966184,-0.888519, 20))
        assertEquals(
            SearchResponseDto(listOf(expectedLocation1, expectedLocation2, expectedLocation3)),
            actualLocations
        )
    }

}