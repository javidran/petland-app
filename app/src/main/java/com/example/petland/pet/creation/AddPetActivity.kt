package com.example.petland.pet.creation

import AnimalSpecies
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
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
    private val listRace: MutableList<String> = ArrayList()
    private val list = ArrayList<String>()

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
        addElementsToSpinnerRace()

    }

    private fun addElementsToSpinnerRace() {

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
                parent?.getItemAtPosition(position)
             //   spinnerRace.setSelection(position).toString()
                Log.d("Spinner", position.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
    fun addElementsToSpinnerSpecies() {
        //llamada a valores de DB
        val query = ParseQuery.getQuery(AnimalSpecies::class.java)
        query.findInBackground { objects, e ->
            if (e == null) {
                for (species in objects) {
                    list.add(species.getDisplayName().toString())
                    Log.d("DEBUG", species.getDisplayName())
                }
            } else {
                Log.d("error", "Error")
            }
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        var SpinnerValue:Spinner=findViewById(R.id.spinnerSpecies)

        SpinnerValue.adapter = adapter
        SpinnerValue.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {
                val item = adapterView.getItemAtPosition(position)
                if (item != null) {
                    Toast.makeText(
                        this@AddPetActivity, item.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Toast.makeText(
                    this@AddPetActivity, "Selected",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
    }
      /*  val array = arrayOfNulls<String>(list.size)
        list.toArray(array)
        val spinner: Spinner = findViewById(R.id.spinnerSpecies)
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, list
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter

        spinner.onItemSelectedListener = this*/


    /*override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        Log.d("selecteditem", pos.toString())
        selectedSpecie(list.get(pos))
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        val text: String = parent.getItemAtPosition(pos).toString()
        Toast.makeText(parent.context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }*/
    private fun selectedSpecie(selection: String) {
        val query = ParseQuery.getQuery(AnimalSpecies::class.java)
        query.whereEqualTo("nameSpecie", selection);
        query.findInBackground { objects, e ->
            if (e == null) {
                for (species in objects) {
                    listRace.add(species.getDisplayName())
                    Log.d("DEBUG", species.getDisplayName())
                }
            } else {
                Log.d("score", "Error: " + e!!.message)
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
