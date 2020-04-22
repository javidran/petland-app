package com.example.petland.user_profile

import com.parse.ParseObject

interface ViewPetCallback {

    fun startViewPetActivity(pet: ParseObject)
}