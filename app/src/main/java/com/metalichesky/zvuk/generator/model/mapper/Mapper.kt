package com.metalichesky.zvuk.generator.model.mapper

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

interface Mapper<I, O> {

    fun map(input: I): O

    fun map(input: Collection<I>): List<O> {
        val result = ArrayList<O>()
        input.mapTo(result) { map(it) }
        return result
    }

    fun wrongFormatException(clazz: KClass<*>, field: KProperty<*>): IllegalStateException {
        return IllegalStateException("Wrong format of ${clazz.simpleName}, ${field.name} is null")
    }

}