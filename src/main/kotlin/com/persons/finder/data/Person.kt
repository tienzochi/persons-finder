package com.persons.finder.data

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "person")
data class Person(
        @Id() @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long,
        @Column(name = "name")
        val name: String
)
