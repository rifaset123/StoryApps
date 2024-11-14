package com.example.storyapps.helper

import com.example.storyapps.data.response.ListStory


interface OnEventClickListener {
    fun onEventClick(event: ListStory)
}