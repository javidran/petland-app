
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Race")
class Race: ParseObject() {
    fun getNameCat(): String {
        return getString("name_ca").toString()
    }
    fun getNameEsp(): String {
        return getString("name").toString()
    }
    fun getNameEn(): String {
        return getString("name_en").toString()
    }
    fun setName(value: String) {
        put("name", value)
    }
}