import com.parse.ParseClassName
import com.parse.ParseObject
import java.util.*

@ParseClassName("AnimalSpecies")
class AnimalSpecies : ParseObject() {
    lateinit var result: String
    fun getDisplayName(): String {
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

    fun setDisplayName(value: String) {
        put("name", value)
    }
}