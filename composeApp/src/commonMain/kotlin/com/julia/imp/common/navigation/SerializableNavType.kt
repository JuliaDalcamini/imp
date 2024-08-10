package com.julia.imp.common.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class SerializableNavType<T>(type: KType) : NavType<T>(type.isMarkedNullable) {

    private val serializer = serializer(type)

    @Suppress("UNCHECKED_CAST")
    override fun parseValue(value: String): T = Json.decodeFromString(serializer, value) as T

    override fun serializeAsValue(value: T): String = Json.encodeToString(serializer, value)

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { parseValue(it) }
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, serializeAsValue(value))
    }
}

inline fun <reified T> serializableNavType() = SerializableNavType<T>(typeOf<T>())