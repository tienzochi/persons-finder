package com.persons.finder.domain.services

import com.persons.finder.custom_exceptions.LocationNotFoundException
import kotlin.math.*
import com.persons.finder.data.Location
import com.persons.finder.data.repository.LocationRepository
import com.persons.finder.dto.SearchResponseDto
import com.persons.finder.dto.SearchResultDto
import com.persons.finder.dto.input.SearchPeopleInputDto
import com.persons.finder.dto.input.UpdateLocationInputDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LocationsServiceImpl @Autowired
constructor(
    private val locationRepository: LocationRepository
) : LocationsService {
    override fun addLocation(id: Long, location: UpdateLocationInputDto) {
        locationRepository.save(
            Location(id, location.latitude, location.longitude)
        )
    }

    override fun findAround(id: Long, data: SearchPeopleInputDto): SearchResponseDto {
        val allLoc = locationRepository.findAll()
        return findNearestLocations(
            data,
            allLoc.filter { it.referenceId != id }
        )
    }

    override fun getLocation(id: Long): Location {
        return locationRepository.findById(id)
            .orElseThrow { LocationNotFoundException("Location for user with id=$id not found") }
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

fun findNearestLocations(
    input: SearchPeopleInputDto,
    locations: List<Location>
): SearchResponseDto {
    return SearchResponseDto(
        getNonNullLocations(locations)
            .map {
                SearchResultDto(
                    it.referenceId,
                    haversine(input.latitude, input.longitude, it.latitude!!, it.longitude!!),
                )
            }
            .filter { it.distanceValue <= input.radiusInKm }
            .sortedBy { it.distanceValue }
    )
}

fun getNonNullLocations(locations: List<Location>): List<Location> {
    return locations
        .filter { it.latitude !== null && it.longitude !== null }
}