
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("AnimalSpecies")
class AnimalSpecies: ParseObject() {
    fun getDisplayName(): String {
        return getString("name").toString()
    }
    fun setDisplayName(value: String) {
        put("name", value)
    }
}