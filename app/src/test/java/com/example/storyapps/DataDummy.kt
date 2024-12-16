package com.example.storyapps

import com.example.storyapps.data.response.ListStory

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStory> {
        val items: MutableList<ListStory> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStory(
                id = i.toString(),
                createdAt = "created at + $i",
                name = "Name $i",
                description = "Description $i",
                photoUrl = "Photo Url $i",
                lat = i.toDouble(),
                lon = i.toDouble()
            )
            items.add(quote)
        }
        return items
    }
}