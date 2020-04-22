
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("AnimalSpecies")
class AnimalSpecies: ParseObject() {
    fun getDisplayNameEsp(): String {
        return getString("name").toString()
    }
    fun getDisplayNameCat(): String {
        return getString("name_ca").toString()
    }
    fun getDisplayNameEn(): String {
        return getString("name_en").toString()
    }
    fun setDisplayName(value: String) {
        put("name", value)
    }
}