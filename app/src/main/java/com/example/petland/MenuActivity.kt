package com.example.petland

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import java.util.*


class MenuActivity : AppCompatActivity(){
   //  var Toolbar = toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
       // Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(Toolbar)

        val buttonCreatePet : Button = findViewById(R.id.buttonCrearPet)

        buttonCreatePet.setOnClickListener { createPet()}

        updateList()
    }



    private fun createPet() {
        val currentUser = ParseUser.getCurrentUser()
        val textPetName = findViewById<EditText>(R.id.editNombrePet)

        val pet = ParseObject("Pet")
        pet.put("name", textPetName.text.toString())
        pet.put("birthday", Calendar.getInstance().time)
        pet.put("chip", 123456)
        pet.put("owner", currentUser)
        val relation = pet.getRelation<ParseUser>("caregivers")
        relation.add(currentUser)
        pet.save()

        textPetName.text.clear()
        updateList()
    }

    private fun updateList() {
        var list = findViewById<TextView>(R.id.petList)
        list.text = ""

        val query = ParseQuery.getQuery<ParseObject>("Pet")
        query.whereEqualTo("owner", ParseUser.getCurrentUser())

        query.findInBackground { result, e ->
            if (e == null) {
                for(el in result) {
                    list.text = (list.text as String).plus("- ".plus(el.objectId).plus(" : ").plus(el.get("name")).plus("\n"))
                }
            } else {
                Log.d("MenuActivity", "PetQuery not completed")
            }
        }


    }
}