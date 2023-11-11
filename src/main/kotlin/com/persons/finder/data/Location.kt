package com.persons.finder.data

import javax.persistence.*

@Entity
@Table(name = "location")
data class Location(
    @Id()
    val referenceId: Long,
    @Column(name = "latitude", nullable = true)
    val latitude: Double?,
    @Column(name = "longitude", nullable = true)
    val longitude: Double?
)
