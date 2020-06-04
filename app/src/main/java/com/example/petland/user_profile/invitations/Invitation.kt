package com.example.petland.user_profile.invitations

import com.example.petland.pet.Pet
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

@ParseClassName("Invitation")
class Invitation : ParseObject() {

    fun getPet() : Pet {
        val pet = getParseObject(PET) as Pet
        pet.fetch<Pet>()
        return pet
    }

    fun getCreator() : ParseUser {
        val user = getParseUser(CREATOR) ?: throw NullPointerException()
        user.fetch()
        return user
    }

    fun setAnswer(answer: Boolean) {
        put(ANSWER, answer)
    }

    companion object {
        private const val CREATOR = "creator"
        private const val RECEIVER = "receiver"
        private const val ANSWER = "answer"
        private const val PET = "petO"

        fun getUnansweredInvitations() : List<Invitation> {
            val query = ParseQuery.getQuery(Invitation::class.java)
            query.whereEqualTo(RECEIVER, ParseUser.getCurrentUser())
            query.whereEqualTo(ANSWER, false)
            return query.find()
        }

        fun existsInvitation(creator: ParseUser, receiver: ParseUser, pet: Pet) : Boolean {
            val query = ParseQuery.getQuery(Invitation::class.java)
            query.whereEqualTo(CREATOR, creator )
            query.whereEqualTo(RECEIVER, receiver)
            query.whereEqualTo(PET, pet)
            return query.find().size != 0
        }

        fun createInvitation(creator: ParseUser, receiver: ParseUser, pet: Pet) {
            val invitation = Invitation()
            invitation.put(CREATOR, creator)
            invitation.put(RECEIVER, receiver)
            invitation.put(PET, pet)
            invitation.put(ANSWER, false)
            invitation.saveInBackground()
        }

    }

}