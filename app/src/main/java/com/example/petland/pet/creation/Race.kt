
import com.parse.ParseClassName
import com.parse.ParseObject
import java.util.*

@ParseClassName("Race")
class Race: ParseObject() {
    lateinit var result: String

    fun getName(): String {
        if(Locale.getDefault().displayLanguage  == "català") {
        result = getString("name_ca").toString()
    }
        else if (Locale.getDefault().displayLanguage == "español") {
            result = getString("name").toString()
        }
        else result = getString("name_en").toString()

        return result
    }
    fun setName(value: String) {
        put("name", value)
    }
}