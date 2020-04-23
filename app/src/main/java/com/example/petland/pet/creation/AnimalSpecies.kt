
import com.parse.ParseClassName
import com.parse.ParseObject
import java.util.*

@ParseClassName("AnimalSpecies")
class AnimalSpecies: ParseObject() {
    lateinit var result: String
    fun getDisplayName(): String {
            if(Locale.getDefault().displayLanguage  == "català") {
                result = getString("name_ca").toString()
            }
            else if (Locale.getDefault().displayLanguage == "español") {
                result = getString("name").toString()
            }
            else result = getString("name_en").toString()

            return result
        }
    fun getNameEsp(): String {
        return getString("name").toString()
    }
    fun setDisplayName(value: String) {
        put("name", value)
    }
}