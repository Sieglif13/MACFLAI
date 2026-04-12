package com.yey.macflai.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringMap(value: Map<String, String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringMap(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, mapType) ?: emptyMap()
    }

    @TypeConverter
    fun fromIntMap(value: Map<String, Int>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toIntMap(value: String): Map<String, Int> {
        val mapType = object : TypeToken<Map<String, Int>>() {}.type
        return gson.fromJson(value, mapType) ?: emptyMap()
    }

    @TypeConverter
    fun fromBooleanList(value: List<Boolean>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toBooleanList(value: String): List<Boolean> {
        val listType = object : TypeToken<List<Boolean>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
}
