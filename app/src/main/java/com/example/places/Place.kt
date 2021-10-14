package com.example.places

import android.graphics.drawable.Drawable
import java.io.Serializable

//class for Places data
data class Place(val uuid: String, val address: String, val code: String, val location : Location, val name: String, val type: String, val tags:  Array<String>, var icon : Drawable): Serializable {
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

     fun setTypeIcon(): Int {
        var icon:Int=0
        when (type) {
            "bar" -> icon = R.drawable.bar_icon_foreground
            "playground" -> icon = R.drawable.play_icon_foreground
            "office" -> icon = R.drawable.office_icon_foreground
            "theater" -> icon = R.drawable.theater_icon_foreground
            "metro_station" -> icon =R.drawable.metro_icon_foreground
            "coffee_shop" -> icon = R.drawable.coffee_icon_foreground
            "restaurant" -> icon = R.drawable.restaurant_icon_foreground
        }
        return icon
    }
}

//inner class for Location only
data class Location(val lat: Double, val lon: Double): Serializable
