package com.example.petland

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_menu.*
import java.util.*


class MenuActivity : AppCompatActivity(){
    private val TAG = "Petland Dashboard"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val buttonCreatePet : Button = findViewById(R.id.buttonCrearPet)
        buttonCreatePet.setOnClickListener { createPet()}
        updateList()



        deletUserButton.setOnClickListener {
            deleteUser()
        }
    }

    private fun deleteUser() {
        val user = ParseUser.getCurrentUser()
        val query = ParseQuery.getQuery<ParseObject>("Pet")
        query.whereEqualTo("owner", user)
        query.findInBackground { petsList, e ->
            if (e == null) {
                for (pet in petsList) {
                    pet.deleteInBackground { e ->
                        if (e == null) {
                            Log.d(TAG,"Pet correctly deleted!") //Mensaje en logcat
                        } else {
                            Log.d(TAG,"An error occurred while deleting a pet!") //Mensaje en logcat
                        }
                    }
                }
            } else {
                Log.d(TAG, "An error happened while retrieving user pets.")
            }
        }
        user.deleteInBackground { e ->
            if (e == null) {
                Log.d(TAG,"User correctly deleted!") //Mensaje en logcat
                ParseUser.logOut()
                Toast.makeText(this, getString(R.string.loggedOut), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java).apply { //Para pasar de esta vista, de nuevo al SignIn
                }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            } else {
                Log.d(TAG,"An error occurred!") //Mensaje en logcat
            }
        }
    }


    fun logOut(view: View){
        //Paso view porque se llama desde el boton (en acivity_menu.xml)
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser != null) {
            Log.d(TAG,getString(R.string.loggedOut)) //Mensaje en logcat
            ParseUser.logOut()
            Toast.makeText(this, getString(R.string.loggedOut), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java).apply { //Para pasar de esta vista, de nuevo al SignIn
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
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
        val list = findViewById<TextView>(R.id.petList)
        list.text = ""

        val query = ParseQuery.getQuery<ParseObject>("Pet")
        query.whereEqualTo("owner", ParseUser.getCurrentUser())

        query.findInBackground { result, e ->
            if (e == null) {
                for(el in result) {
                    list.text = (list.text as String).plus("- ".plus(el.objectId).plus(" : ").plus(el.get("name")).plus("\n"))
                }
            } else {
                Log.d(TAG, "PetQuery not completed")
            }
        }
    }

    fun editProfile(view: View) {
        val intent = Intent(this, EditProfileActivity::class.java).apply {
        }

        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    // Test //////////////////////////////////////////////////////////
    fun goToCreatePet(view: View) {
        val intent = Intent(this, GetFirstPetActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
    // End Of Test //////////////////////////////////////////////////////////
}