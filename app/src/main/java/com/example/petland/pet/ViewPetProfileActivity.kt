package com.example.petland.pet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.R
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_edit_pet_profile.*
import kotlinx.android.synthetic.main.activity_view_pet_profile.*
import kotlinx.android.synthetic.main.activity_view_pet_profile.textViewName
import kotlinx.android.synthetic.main.activity_view_pet_profile.textViewOwner
import java.text.SimpleDateFormat
import java.util.*

class ViewPetProfileActivity : AppCompatActivity() {

    private lateinit var myPet:ParseObject
    private val TAG = "Petland EditPetProfile"

    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pet_profile)
        setData()
    }

    private fun setData() {
        val pets = ParseQuery.getQuery<ParseObject>("Pet")
        pets.whereEqualTo("name", "Julia")
        myPet = pets.first
        val caregivers: ParseRelation<ParseUser> = myPet.getRelation<ParseUser>("caregivers")
        val listCaregivers = caregivers.query

        textViewName.text = myPet.get("name").toString()
        val dateb = sdf.format(myPet.get("birthday"))
        textViewBirth.text = dateb.toString()
        textViewChip.text = myPet.get("chip").toString()

        val list = findViewById<TextView>(R.id.petCaregiversList)
        list.text = ""
        listCaregivers.findInBackground { result, e ->
            if (e == null) {
                for (el in result) {
                    list.text = (list.text as String).plus("- ".plus(el.username).plus("\n"))
                    if (el.objectId == myPet.get("owner").toString()) textViewOwner.text = el.username
                }
            } else {
                Log.d(TAG, "PetQuery not completed")
            }
        }
    }

    fun volver(view: View) {
        val intent = Intent(this, MenuActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun editPetProfile(view: View) {

        val intent = Intent(this, EditPetProfileActivity::class.java).apply {
        }
        intent.putExtra("petId", myPet.objectId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    companion object {
        private const val TAG = "Petland EditPetProfile"

    }

}
