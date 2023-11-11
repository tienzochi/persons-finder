package com.persons.finder.domain.services

import kotlin.math.*
import com.persons.finder.data.Location
import com.persons.finder.data.repository.LocationRepository
import com.persons.finder.dto.SearchResponseDto
import com.persons.finder.dto.UpdateLocationMapperImpl
import com.persons.finder.dto.input.SearchPeopleInputDto
import com.persons.finder.dto.input.UpdateLocationInputDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LocationsServiceImpl @Autowired
constructor(
    private val locationRepository: LocationRepository,
    private val updateLocationMapperImpl: UpdateLocationMapperImpl
) : LocationsService {
    override fun addLocation(location: UpdateLocationInputDto) {
        locationRepository.save(updateLocationMapperImpl.toEntity(location))
    }

    override fun findAround(data: SearchPeopleInputDto): SearchResponseDto {
        val allLoc = locationRepository.findAll()
        return findNearestLocations(data, allLoc)
    }

}

fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371.0 // Radius of the Earth in kilometers
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c
}

fun findNearestLocations(input: SearchPeopleInputDto, locations: List<Location>): SearchResponseDto {
    return SearchResponseDto(locations.filter {
        it.latitude !== null && it.longitude !== null &&
        haversine(input.latitude, input.longitude, it.latitude, it.longitude) <= input.radiusInKm
    }.map { it.referenceId })
}