package com.persons.finder.dto

interface Mapper<T,V> {
    fun toDto(entity: T): V
    fun toEntity(dto: V): T
}