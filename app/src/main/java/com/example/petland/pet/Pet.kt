package com.example.petland.pet

import AnimalSpecies
import Race
import android.graphics.Bitmap
import android.util.Log
import com.example.petland.Application
import com.example.petland.events.model.PetEvent
import com.example.petland.locations.model.PetlandLocation
import com.parse.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

@ParseClassName("Pet")
class Pet : ParseObject() {

    fun setName(name: String) {
        put(NAME, name)
    }

    fun getName() : String {
        return getString("name") ?: throw NullPointerException()
    }

    fun setBirthday(date: Date) {
        put(BIRTHDAY, date)
    }

    fun hasBirthday() : Boolean {
        return containsKey(BIRTHDAY)
    }

    fun getBirthday() : Date {
        return getDate(BIRTHDAY) ?: throw NullPointerException()
    }

    fun setChipNumber(number: Int) {
        put(CHIP, number)
    }

    fun hasChipNumber() : Boolean {
        return containsKey(CHIP)
    }

    fun getChipNumber() : Int {
        return getInt(CHIP)
    }

    fun setOwner(user: ParseUser) {
        put(OWNER, user)
        addCaregiver(user)
    }

    private fun getOwner(withFetch: Boolean) :ParseUser {
        val owner = getParseUser(OWNER) ?: throw NullPointerException()
        if (withFetch) owner.fetch()
        return owner
    }

    fun getOwner() : ParseUser {
        return getOwner(true)
    }

    fun isOwner(user: ParseUser): Boolean {
        return getOwner(false).objectId == user.objectId
    }

    fun addCaregiver(user: ParseUser) {
        getRelation<ParseUser>(CAREGIVER).add(user)
    }

    fun removeCaregiver(user: ParseUser) {
        getRelation<ParseUser>(CAREGIVER).remove(user)
        saveInBackground()
    }

    fun getCaregivers() : List<ParseUser> {
        return getCaregiversFromPet(this)
    }

    fun getCaregiversQuery() : ParseQuery<ParseUser> {
        return getCaregiversQueryFromPet(this)
    }

    fun setImage(image: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        val file = ParseFile("profileImage.png", bytes)
        file.save()
        put(IMAGE, file)
    }

    fun removeImage() {
        remove(IMAGE)
    }

    fun setSpecie(specie: AnimalSpecies) {
        put(SPECIE, specie)
    }

    fun getSpecie() : AnimalSpecies {
        val specie = getParseObject(SPECIE) as AnimalSpecies
        specie.fetch<AnimalSpecies>()
        return specie
    }

    fun setFirstRace(race: Race) {
        put(RACE1, race)
    }

    fun getFirstRace() : Race {
        val race = getParseObject(RACE1) as Race
        race.fetch<Race>()
        return race
    }

    fun setSecondRace(race: Race) {
        put(RACE2, race)
    }

    fun hasSecondRace() : Boolean {
        return containsKey(RACE2)
    }

    fun getSecondRace() : Race {
        val race = getParseObject(RACE2) as Race
        race.fetch<Race>()
        return race
    }

    fun removeSecondRace() {
        remove(RACE2)
    }

    fun setVeterinary(veterinary: PetlandLocation) {
        put(VET, veterinary)
    }

    fun hasVeterinary() : Boolean {
        return containsKey(VET)
    }

    fun getVeterinary() : PetlandLocation {
        val vet = getParseObject(VET) as PetlandLocation
        vet.fetch<PetlandLocation>()
        return vet
    }


    fun savePet() {
        save()
    }



    companion object {
        private val TAG = "Petland Pets"

        private const val NAME = "name"
        private const val BIRTHDAY = "birthday"
        private const val CHIP = "chip"
        private const val OWNER = "owner"
        private const val CAREGIVER = "caregivers"
        private const val SPECIE = "nameSpecie"
        private const val RACE1 = "nameRace"
        private const val RACE2 = "nameRaceOpt"
        private const val VET = "veterinarian"
        private const val IMAGE = "image"

        private lateinit var selectedPet : Pet

        fun getPetsFromCurrentUser(): List<Pet> {
            val query = ParseQuery.getQuery(Pet::class.java)
            query.whereEqualTo(CAREGIVER, ParseUser.getCurrentUser())
            val result : List<Pet> = query.find()
            if(result.isEmpty()) {
                Application.startNoPetsActivity()
            }
            else if (!this::selectedPet.isInitialized) {
                selectedPet = result[0]
            }
            return result
        }
        fun userHasPets(): Boolean {
            val query = ParseQuery.getQuery(Pet::class.java)
            query.whereEqualTo(CAREGIVER, ParseUser.getCurrentUser())
            val result = query.find()
            if(result.isNotEmpty() && !this::selectedPet.isInitialized) {
                selectedPet = result[0]
            }
            return result.isNotEmpty()
        }

        fun setSelectedPet(pet: Pet) {
           selectedPet = pet
        }

        fun getSelectedPet(): Pet {
            if(!this::selectedPet.isInitialized) getPetsFromCurrentUser()
            return selectedPet
        }

        fun getNamesFromPetList(pets: List<Pet>) : Array<String> {
            val names: ArrayList<String> = ArrayList()
            for(p in pets) {
                val name: String = (p.get("name") ?: throw NullPointerException("Pet name should not be null")) as String
                names.add(name)
            }
            val array = arrayOfNulls<String>(pets.size)
            return names.toArray(array)
        }
        fun getIdFromPetsList(pets: List<Pet>) : Array<String> {
            val names: ArrayList<String> = ArrayList()
            for(p in pets) {
                val name: String = (p.objectId ?: throw NullPointerException("Petid should not be null"))
                names.add(name)
            }
            val array = arrayOfNulls<String>(pets.size)
            return names.toArray(array)
        }
        fun deletePet(pet: Pet) {
            PetEvent.getEventsFromPet(pet).forEach { e -> e.deleteEvent() }
            pet.deleteInBackground { e ->
                if (e == null) {
                    Log.d(TAG, "Pet correctly deleted!")
                } else {
                    Log.d(TAG, "An error occurred while deleting a pet!")
                }
            }
        }

        fun getCaregiversFromPet(pet: Pet) : List<ParseUser> {
            return pet.getRelation<ParseUser>(CAREGIVER).query.find()
        }

        fun getCaregiversQueryFromPet(pet: Pet) : ParseQuery<ParseUser> {
            return pet.getRelation<ParseUser>(CAREGIVER).query
        }

        fun getCaregiversNamesFromPet(pet: Pet) : List<String> {
            val names = ArrayList<String>()
            for(l in getCaregiversFromPet(pet)) {
                val name = (l.getString("name") ?: throw NullPointerException("User name should not be null"))
                names.add(name)
            }
            return names
        }

    }

}