package com.persons.finder

import com.persons.finder.custom_exceptions.PersonNotFoundException
import com.persons.finder.data.Location
import com.persons.finder.data.Person
import com.persons.finder.data.repository.LocationRepository
import com.persons.finder.data.repository.PersonRepository
import com.persons.finder.domain.services.PersonsServiceImpl
import com.persons.finder.dto.GetPersonByIdResponseDto
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PersonServiceImplTests {
    private val personRepositoryMock = mock(PersonRepository::class.java)
    private val locationRepositoryMock = mock(LocationRepository::class.java)
    private val personServiceImpl = PersonsServiceImpl(
        personRepositoryMock,
        locationRepositoryMock,
    )

    private val mockPerson1 = Person(1, "Jon")
    private val mockPerson2 = Person(2, "Arya")
    private val mockPerson3 = Person(3, "Sansa")

    @AfterEach
    fun afterEach() {
        reset(personRepositoryMock, locationRepositoryMock)
    }

    @Test
    fun `Should get multiple ids`() {
        val expectedList = listOf(
            mockPerson1,
            mockPerson2,
            mockPerson3
        )
        `when`(personRepositoryMock.findAllById(listOf(1,2,3)))
            .thenReturn(expectedList)
        val result = personServiceImpl.getByMultipleId(listOf(1,2,3))
        assertEquals(expectedList, result)
    }

    @Test
    fun `Should return empty list if ids provided has no locations`() {
        `when`(personRepositoryMock.findAllById(listOf(1,2,3)))
            .thenReturn(listOf())
        val result = personServiceImpl.getByMultipleId(listOf(1,2,3))
        assertEquals(listOf<Person>(), result)
    }

    @Test
    fun `Should get one by id`() {
        `when`(personRepositoryMock.findById(1))
            .thenReturn(Optional.of(mockPerson1))
        val result = personServiceImpl.getOneById(1)
        assertEquals(Person(1, "Jon"), result)
    }

    @Test
    fun `Should return PersonNotFoundException person with id not found`() {
        assertThrows<PersonNotFoundException> {
            personServiceImpl.getOneById(1)
        }
    }

    @Test
    fun `Should return a list with one person if only one id`() {
        val expectedPerson = GetPersonByIdResponseDto(1, "Jon")
        `when`(personRepositoryMock.findById(1))
            .thenReturn(Optional.of(mockPerson1))
        val result = personServiceImpl.getById(listOf(1))
        assertEquals(listOf(expectedPerson), result)
    }

    @Test
    fun `Should call save() on person and location repository on create`() {
        `when`(personRepositoryMock.save(mockPerson3))
            .thenReturn(mockPerson3)

        personServiceImpl.save(mockPerson3)
        verify(personRepositoryMock, times(1)).save(mockPerson3)
        verify(locationRepositoryMock, times(1)).save(Location(3, null, null))
    }
}