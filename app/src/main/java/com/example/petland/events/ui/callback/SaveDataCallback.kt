package com.example.petland.events.ui.callback

import com.parse.ParseObject

interface SaveDataCallback {
    fun checkAndSaveData() :ParseObject?
}