package com.example.petland.events.ui

import com.example.petland.events.model.PetEvent

interface ViewEventCallback {

    fun startViewEventActivity(event: PetEvent)
}