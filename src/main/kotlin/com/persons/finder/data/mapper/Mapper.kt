package com.persons.finder.data.mapper

interface Mapper<T,V> {
    fun toDto(entity: T): V
    fun toEntity(dto: V): T
}