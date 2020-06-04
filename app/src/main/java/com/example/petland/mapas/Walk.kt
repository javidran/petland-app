package com.example.petland.mapas

import com.example.petland.events.model.PetEvent
import com.example.petland.events.model.WalkEvent
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import java.text.SimpleDateFormat
import java.util.*


@ParseClassName("Walk")
class Walk  : ParseObject() {
    fun getTime(): Int {
        return getInt("duration")
    }
    fun getRecorrido(): String {
        return getString("distance").toString()
    }
    fun createWalk(
         dateEnd: Date, dateIni: Date, time: Int,
        distance: String, latitudes: MutableList<Double>,
        longitudes: MutableList<Double>, selection: String, listEvents: ArrayList<PetEvent>, pet: String
    ) {
        val query = ParseQuery.getQuery<ParseObject>("Pet")
        query.whereEqualTo("name", pet)
       val  result = query.find().first()
        val relation = this.getRelation<ParseObject>("pets")
        put("user", ParseUser.getCurrentUser())
        relation.add(result)
        put("user", ParseUser.getCurrentUser())
        put("startDate", dateIni)
        put("endDate", dateEnd)
        put("duration", time)
        put("distance", distance)
        put("locLatitudes",latitudes)
        put("locLongitudes", longitudes)
        if (selection != "No asignar a ningún evento" && selection != "") {
            for (event in listEvents) {
                val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.US)
                val date = event.getDate()
                if (selection == sdf.format(date)) {
                    val walkEv = event.getData() as WalkEvent
                    walkEv.setWalk(this)
                    event.markAsDone(dateEnd)
                }
            }
        }
        saveInBackground()
    }
}