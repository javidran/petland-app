package com.example.petland.events.ui

import com.parse.ParseObject

interface SaveDataCallback {
    fun checkAndSaveData() :ParseObject?
}