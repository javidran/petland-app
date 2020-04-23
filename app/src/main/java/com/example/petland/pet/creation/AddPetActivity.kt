package com.example.petland.pet.creation
import AnimalSpecies
import Race
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.image.ImageOnCreationActivity
import com.parse.*
import kotlinx.android.synthetic.main.activity_add_pet.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class AddPetActivity : AppCompatActivity() {
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date
    lateinit var raceobj: ParseObject
    lateinit var raceobjopt: ParseObject
    lateinit var animalspeciesobj: ParseObject
    lateinit var check: CheckBox
    private var checkedRace: Boolean = false

    lateinit var profileImage : ImageView
    var image : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        profileImage = findViewById(R.id.profileImage)
        val cal = Calendar.getInstance()
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
        profileImage.setOnClickListener { chooseImage() }

    }

    fun addElementsToSpinnerSpecies() {
        //llamada a valores de DB
        val list = ArrayList<String>()
        val query = ParseQuery.getQuery(AnimalSpecies::class.java)
        val objects = query.find()
        if (objects != null) {
            for (species in objects) {
                list.add(species.getDisplayNameEsp())
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
                 animalspeciesobj = query.find().first()
                addElementsToSpinnerRace(animalspeciesobj)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }



    private fun addElementsToSpinnerRace(animalSpecies: ParseObject) {
        val listRace: ArrayList<String> = ArrayList()
        val query = ParseQuery.getQuery(Race::class.java)
       query.whereEqualTo("nameSpecie",  animalSpecies )
        val objects = query.find()
        if (objects != null) {
            for (species in objects) {
                listRace.add(species.getNameEsp())
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
                query.whereEqualTo("name", parent?.getItemAtPosition(position).toString())
                raceobj = query.find().first()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val dataAdapteropt = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, listRace
        )
        dataAdapteropt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRaceopt.adapter = dataAdapteropt
        spinnerRaceopt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("Spinner", position.toString())
                query.whereEqualTo("name", parent?.getItemAtPosition(position).toString())
                raceobjopt = query.find().first()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }


    fun createPet(view: View) {
        val textPetName = findViewById<EditText>(R.id.editTextPetname)
        val chipNumber = findViewById<EditText>(R.id.editTextChip)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        when {
            TextUtils.isEmpty(textPetName.text) -> {
                textPetName.error = getString(R.string.petNameNeeded)
            }
            else -> {
                val currentUser = ParseUser.getCurrentUser()

                val pet = ParseObject("Pet")
                pet.put("name", textPetName.text.toString())
                if (!TextUtils.isEmpty(textViewBirthday.text)) pet.put("birthday", date)
                if (!TextUtils.isEmpty(chipNumber.text)) pet.put(
                    "chip",
                    Integer.valueOf(chipNumber.text.toString())
                )
                pet.put("owner", currentUser)
                putImage(pet)
                pet.put("nameSpecie", animalspeciesobj)
                if (checkedRace && raceobj.objectId == raceobjopt.objectId) {
                        Toast.makeText(
                            this@AddPetActivity,
                            "Razas iguales",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                else {
                    if (checkedRace) pet.put("nameRaceopt", raceobjopt)
                    pet.put("nameRace", raceobj)
                    val relation = pet.getRelation<ParseUser>("caregivers")
                    relation.add(currentUser)
                    pet.save()

                    Log.d(TAG, "Profile created correctly")

                    val intent = Intent(this, HomeActivity::class.java).apply {}
                    startActivity(intent)
                    finish()
                    overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                }
            }
        }

    }
    private fun putImage(pet: ParseObject) {
        if(image != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            image!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            val file = ParseFile("profileImage.png", bytes)
            file.save()
            pet.put("image", file)
        } else {
            pet.remove("image")
        }
    }

    private fun chooseImage() {
        val intent = Intent(this, ImageOnCreationActivity::class.java).apply {}
        startActivityForResult(intent, ImageOnCreationActivity.PICK_IMAGE)
    }


    fun volver(view: View) {
        finish()
    }



    fun checkVisibility(view: View) {
        if (view is CheckBox) {
             checkedRace = view.isChecked

            when (view.id) {
                R.id.checkBox -> {
                    if (checkedRace) {
                        spinnerRaceopt.visibility = View.VISIBLE
                    } else {
                        spinnerRaceopt.visibility = View.INVISIBLE
                    }
                }

            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ImageOnCreationActivity.PICK_IMAGE) {
            if(resultCode == ImageOnCreationActivity.USE_DEFAULT) {
                image = null
                profileImage.setImageDrawable(getDrawable(R.drawable.animal_paw))
            } else if (resultCode == RESULT_OK) {
                val uri = data?.extras?.get("image") as Uri?
                image = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                profileImage.setImageBitmap(image)
            }
        }
    }

    companion object {
        private const val TAG = "Petland AddPet"
    }

}


