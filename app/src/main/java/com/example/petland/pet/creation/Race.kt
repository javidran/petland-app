
import com.parse.ParseClassName
import com.parse.ParseObject

@ParseClassName("Race")
class Race: ParseObject() {
    fun getName(): String {
        return getString("name").toString()
    }
    fun setName(value: String) {
        put("name", value)
    }
}