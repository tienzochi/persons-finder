package com.persons.finder.presentation

import com.persons.finder.domain.services.LocationsServiceImpl
import com.persons.finder.dto.GetLocationResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/locations")
class LocationController @Autowired constructor(
    private val locationsServiceImpl: LocationsServiceImpl
) {
    @GetMapping()
    @ResponseBody
    fun getLocations(): List<GetLocationResponseDto> {
        return locationsServiceImpl.getLocations()
    }
}