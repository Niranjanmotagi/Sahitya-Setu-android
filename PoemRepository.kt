package com.example.app.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.app.model.Poem
import com.example.app.model.Poet

object PoemRepository {

    fun getPoemOfTheDay(poems: List<Poem>): Poem {
        val day = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        return poems[day % poems.size]
    }

    fun loadPoems(context: Context): List<Poem> {
        val json = context.assets.open("poems.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Poem>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun loadPoets(context: Context): List<Poet> {
        val json = context.assets.open("poets.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Poet>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun loadSpecialPoems(context: Context): List<Poem> {
        val json = context.assets.open("special_poems.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Poem>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun saveFavorites(context: Context, favorites: Set<Int>) {
        val prefs = context.getSharedPreferences("poem_prefs", Context.MODE_PRIVATE)
        prefs.edit().putStringSet("favorites", favorites.map { it.toString() }.toSet()).apply()
    }

    fun loadFavorites(context: Context): Set<Int> {
        val prefs = context.getSharedPreferences("poem_prefs", Context.MODE_PRIVATE)
        val set = prefs.getStringSet("favorites", emptySet()) ?: emptySet()
        return set.map { it.toInt() }.toSet()
    }
}
