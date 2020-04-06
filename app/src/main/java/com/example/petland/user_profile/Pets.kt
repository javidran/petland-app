package com.example.petland.user_profile

import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class Pets {

    fun getPets(): MutableList<ParseObject>? {
        val query = ParseQuery.getQuery<ParseObject>("Pet")
        query.whereEqualTo("caregivers", ParseUser.getCurrentUser())
        return query.find();
    }
}