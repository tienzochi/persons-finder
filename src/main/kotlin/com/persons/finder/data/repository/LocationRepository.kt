package com.persons.finder.data.repository

import com.persons.finder.data.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository: JpaRepository<Location, Long>