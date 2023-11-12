package com.persons.finder

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.persons.finder.data.Location
import com.persons.finder.data.Person
import com.persons.finder.data.repository.LocationRepository
import com.persons.finder.data.repository.PersonRepository
import com.persons.finder.dto.SearchResultDto
import com.persons.finder.dto.input.UpdateLocationInputDto
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.mockito.Mockito.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.http.MediaType
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var personRepositoryMock: PersonRepository

    @MockBean
    private lateinit var locationRepositoryMock: LocationRepository

    @AfterEach
    fun afterEach() {
        reset(personRepositoryMock)
    }

    @Test
    fun createPersonTest() {
        `when`(personRepositoryMock.save(Person(1, "Jon")))
            .thenReturn(Person(1, "Jon"))

        mockMvc.perform(
            post("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(Person(1, "Jon")))

        ).andExpect(status().isCreated)
    }

    @Test
    fun `Should get all persons if param is not provided`() {
        `when`(personRepositoryMock.findAll())
            .thenReturn(listOf(
                Person(1, "Jon"),
                Person(2, "Arya"),
                Person(3, "Sansa")
            ))

        mockMvc
            .perform(get("/api/v1/persons"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(3))
    }

    @Test
    fun `Should return multiple persons by provided ids`() {
        `when`(personRepositoryMock.findAllById(listOf(10,5)))
            .thenReturn(listOf(
                Person(10, "Jon"),
                Person(5, "Sansa")
            ))

        mockMvc
            .perform(get("/api/v1/persons").param("id", "10,5"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    fun `Should one persons when id is provided`() {
        `when`(personRepositoryMock.findById(1))
            .thenReturn(Optional.of(Person(1, "Jon")))

        mockMvc
            .perform(get("/api/v1/persons").param("id", "1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(1))
    }

    @Test
    fun searchByRadiusTest() {
        `when`(locationRepositoryMock.findById(1))
            .thenReturn(Optional.of(Location(1, 50.966184, -0.888519)))
        `when`(locationRepositoryMock.findAll())
            .thenReturn(listOf(
                Location(1, 50.966184,-0.888519),
                Location(2, 51.011136,-0.969543),
                Location(3, 51.002495,-1.028595),
                Location(4, 51.040503,-1.084900)
            ))

        mockMvc
            .perform(get("/api/v1/persons/1/search").param("radiusInKm", "40"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.results").isArray)
            .andExpect(jsonPath("$.results.length()").value(3))
    }

    @Test
    fun updateLocationTest() {
        mockMvc
            .perform(
                put("/api/v1/persons/1/locations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(UpdateLocationInputDto(51.0, -1.0)))
            ).andExpect(status().isNoContent)
    }
}