package com.example.petland.mapas

import com.example.petland.events.model.PetEvent
import com.example.petland.events.model.WalkEvent
import com.parse.*
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
        longitudes: MutableList<Double>, selection: String, listEvents: ArrayList<PetEvent>, relation: ParseRelation<ParseObject>, result: ParseObject
    ) {
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
        save()
    }
}