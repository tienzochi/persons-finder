package com.persons.finder

import com.persons.finder.data.Location
import com.persons.finder.data.repository.LocationRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var locationRepositoryMock: LocationRepository

    @Test
    fun testGetLocations() {
        `when`(locationRepositoryMock.findAll())
            .thenReturn(
                listOf(
                    Location(1, 51.0, -1.0),
                    Location(2, 51.1, -1.1),
                    Location(3, 51.2, -1.2)
                )
            )
        mockMvc.perform(get("/api/v1/locations"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(3))
    }
}