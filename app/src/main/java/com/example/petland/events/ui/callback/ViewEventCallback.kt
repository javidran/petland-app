package com.example.petland.events.ui.callback

import com.example.petland.events.model.PetEvent

interface ViewEventCallback {

    fun startViewEventActivity(event: PetEvent)
}