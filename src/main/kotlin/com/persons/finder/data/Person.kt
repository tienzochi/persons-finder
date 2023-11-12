package com.persons.finder.data

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "person")
data class Person(
        @Id()
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long,

        @Column(name = "name")
        val name: String,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "locationId", referencedColumnName = "referenceId")
        val location: Location? = null
)
