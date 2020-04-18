package com.example.petland.pet.creation

import AnimalSpecies
import Race
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.HomeActivity
import com.example.petland.R
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_add_pet.*
import java.text.SimpleDateFormat
import java.util.*


class AddPetActivity : AppCompatActivity() {
    private val TAG = "Petland AddPet"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        val cal = Calendar.getInstance()
        //register class
        ParseObject.registerSubclass(AnimalSpecies::class.java)
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId("")
            .clientKey("")
            .server("")
            .build()
        )
        ParseObject.registerSubclass(Race::class.java)
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId("")
            .clientKey("")
            .server("")
            .build()
        )
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date = cal.time
                textViewBirthday.text = sdf.format(cal.time)
            }

        textViewBirthday.setOnClickListener {
            val dialog = DatePickerDialog(
                this@AddPetActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Date().time
            dialog.show()
        }
        addElementsToSpinnerSpecies()


    }

    fun addElementsToSpinnerSpecies() {
        //llamada a valores de DB
        val list = ArrayList<String>()
        val query = ParseQuery.getQuery(AnimalSpecies::class.java)
        val objects = query.find()
        if (objects != null) {
            for (species in objects) {
                list.add(species.getDisplayName())
                Log.d("DEBUG", species.getDisplayName())
            }
        }

        val spinner: Spinner = findViewById(R.id.spinnerSpecies)
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, list
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.getItemAtPosition(position)
                Log.d("Spinner", parent?.getItemAtPosition(position).toString())
                query.whereEqualTo("name", parent?.getItemAtPosition(position).toString())
                val results: ParseObject = query.find().first()
                addElementsToSpinnerRace(results)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }



    private fun addElementsToSpinnerRace(animalSpecies: ParseObject) {
        val listRace: ArrayList<String> = ArrayList()
        val query = ParseQuery.getQuery(Race::class.java)
       // val razas = query.find()
       query.whereEqualTo("nameSpecie",  animalSpecies )
        val objects = query.find()
        if (objects != null) {
            for (species in objects) {
                listRace.add(species.getName())
            }
        }

        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, listRace
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRace.adapter = dataAdapter
        spinnerRace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("Spinner", position.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }


    fun createPet(view: View) {
        val currentUser = ParseUser.getCurrentUser()
        val textPetName = findViewById<EditText>(R.id.editTextPetname)
        val chipNumber = findViewById<EditText>(R.id.editTextChip)

        val pet = ParseObject("Pet")
        pet.put("name", textPetName.text.toString())
        pet.put("birthday", date)
        pet.put("chip", Integer.valueOf(chipNumber.text.toString()))
        pet.put("owner", currentUser)
        val relation = pet.getRelation<ParseUser>("caregivers")
        relation.add(currentUser)
        pet.save()

        Log.d(TAG, "Profile created correctly")

        textPetName.text.clear()
        chipNumber.text.clear()

        val intent = Intent(this, HomeActivity::class.java).apply {}
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }


    fun volver(view: View) {
        finish()
    }

}
