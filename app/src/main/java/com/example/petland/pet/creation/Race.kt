import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery
import java.util.*

@ParseClassName("Race")
class Race : ParseObject() {
    lateinit var result: String

    fun getName(): String {
        result = when (Locale.getDefault().displayLanguage) {
            "català" -> {
                getString("name_ca").toString()
            }
            "español" -> {
                getString("name").toString()
            }
            else -> getString("name_en").toString()
        }

        return result
    }

    fun setName(value: String) {
        put("name", value)
    }

    companion object {
        private const val SPECIE = "nameSpecie"

        fun getRaces(specie: AnimalSpecies) : List<Race> {
            val query = ParseQuery.getQuery(Race::class.java)
            query.whereEqualTo("nameSpecie",  specie)
            return query.find()
        }
    }
}