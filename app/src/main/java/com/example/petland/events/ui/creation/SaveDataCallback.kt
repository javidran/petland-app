package com.example.petland.events.ui.creation

import com.parse.ParseObject

interface SaveDataCallback {
    fun checkAndSaveData() :ParseObject?
}