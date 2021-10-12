package com.example.places

import java.io.Serializable

//class for Places data
data class Place(val uuid: String, val address: String, val code: String, val location : Location, val name: String, val type: String, val tags:  Array<String>): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Place

        if (!tags.contentEquals(other.tags)) return false

        return true
    }

    override fun hashCode(): Int {
        return tags.contentHashCode()
    }
}

//inner class for Location only
data class Location(val lat: Double, val lon: Double): Serializable
