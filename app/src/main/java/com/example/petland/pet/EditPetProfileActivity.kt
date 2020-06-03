package com.example.petland.pet

import AnimalSpecies
import Race
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.image.ImageActivity
import com.example.petland.image.ImageUtils
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_edit_pet_profile.*
import java.text.SimpleDateFormat
import java.util.*


class EditPetProfileActivity : AppCompatActivity(), ViewCaregiversCallback {

    private lateinit var myPet:ParseObject
    private val TAG = "Petland EditPetProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date
    private var owner: Boolean = false
    private lateinit var raceObj: ParseObject
    private lateinit var raceObjOpt: ParseObject
    private lateinit var animalSpecies: AnimalSpecies

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.example.petland.R.layout.activity_edit_pet_profile)
        profileImageView1.setOnClickListener { seeImage() }
        myPet = intent.extras?.get("petId") as ParseObject
        setData()
    }

    override fun onResume() {
        super.onResume()
        setData()
        verImagen()
        updateCaregivers()
    }

    private fun verImagen () {
        ImageUtils.retrieveImage(myPet, profileImageView1)
    }

    private fun setData() {

        val user = ParseUser.getCurrentUser()
        myPet.fetch<ParseObject>()

        val listUsers = ParseQuery.getQuery<ParseUser>("_User")
        val powner = myPet.get("owner") as ParseObject
        listUsers.whereEqualTo("objectId", powner.objectId)

        usernameText1.text = myPet.getString("name")
        ownerText1.text = listUsers.first.get("username").toString()
        owner = (ownerText1.text == user.username)

        val listRaces = ParseQuery.getQuery<ParseObject>("Race")
        val pRacePrincipal = myPet.get("nameRace") as ParseObject
        listRaces.whereEqualTo("objectId", pRacePrincipal.objectId)


        val textViewBirthday: TextView = findViewById(R.id.birthText1)
        val cal = Calendar.getInstance()
        var birth = myPet.get("birthday")
        date = birth as Date
        if(birth!=null) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            birth = sdf.format(birth)
            birthText1.setText(birth.toString())
        }
        else birthText1.setText("")




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
                this@EditPetProfileActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Date().time
            dialog.show()
        }


        val chipText = myPet.get("chip")
        if(chipText!=null) {
            chipText1.setText(chipText.toString())
        }
        else chipText1.setText("")

        updateCaregivers()

        if (owner) {     // enable buttons
            val deleteButton:TextView = findViewById(R.id.deleteButton)
            deleteButton.visibility = View.VISIBLE
            val addCarg:TextView = findViewById(R.id.addCarg)
            addCarg.visibility = View.VISIBLE
        }
        verImagen()
        addElementsToSpinnerRace()
        addElementsToSpinnerRaceOpt()
        if(myPet.getParseObject("nameRaceopt") != null) {
            buttonAddRace.visibility = View.GONE
            buttonDelRace.visibility = View.VISIBLE
            spinnerRace2.visibility = View.VISIBLE
        }
    }

    private fun addElementsToSpinnerRace() {
        val listRace: ArrayList<String> = ArrayList()
        val query = ParseQuery.getQuery(Race::class.java)
        query.whereEqualTo("nameSpecie",  myPet.get("nameSpecie"))
        val myRace:ParseObject = myPet.get("nameRace") as ParseObject
        val objects = query.find()
        var i = 0
        if (objects != null) {
            var found = false
            for (species in objects) {
                listRace.add(species.getName())
                if(species.objectId == myRace.objectId) found = true

                if(!found) ++i
            }
        }

        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, listRace
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRace1.adapter = dataAdapter
        spinnerRace1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (Locale.getDefault().displayLanguage) {
                    "català" -> {
                        query.whereEqualTo("name_ca", parent?.getItemAtPosition(position).toString())
                    }
                    "español" -> {
                        query.whereEqualTo("name", parent?.getItemAtPosition(position).toString())
                    }
                    else -> query.whereEqualTo("name_en", parent?.getItemAtPosition(position).toString())
                }
                raceObj = query.find().first()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        spinnerRace1.setSelection(i)
    }

    fun editRace(view: View) {
        if (view.id == R.id.buttonAddRace) {
            buttonAddRace.visibility = View.GONE
            buttonDelRace.visibility = View.VISIBLE
            spinnerRace2.visibility = View.VISIBLE
        }
        else {
            buttonAddRace.visibility = View.VISIBLE
            buttonDelRace.visibility = View.GONE
            spinnerRace2.visibility = View.GONE
        }
    }

    private fun addElementsToSpinnerRaceOpt() {
        val listRace: ArrayList<String> = ArrayList()
        val query = ParseQuery.getQuery(Race::class.java)
        query.whereEqualTo("nameSpecie",  myPet.get("nameSpecie"))
        val num: Int = query.count()
        val myRace: ParseObject? = myPet.getParseObject("nameRaceopt")
        val objects = query.find()
        var i = 0
        if (objects != null) {
            var found = false
            for (species in objects) {
                listRace.add(species.getName())
                if(myRace != null && species.objectId == myRace.objectId) found = true

                if(myRace != null && !found) ++i
            }
        }

        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, listRace
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRace2.adapter = dataAdapter
        spinnerRace2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (Locale.getDefault().displayLanguage) {
                    "català" -> {
                        query.whereEqualTo("name_ca", parent?.getItemAtPosition(position).toString())
                    }
                    "español" -> {
                        query.whereEqualTo("name", parent?.getItemAtPosition(position).toString())
                    }
                    else -> query.whereEqualTo("name_en", parent?.getItemAtPosition(position).toString())
                }
                raceObjOpt = query.find().first()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        spinnerRace2.setSelection(i)
    }

    fun wantToDeletePet(view: View) {
        deletionDialog(view)
    }

    fun deletePet(view: View) {
        Pets.deletePet(myPet)
        val intent = Intent(this, HomeActivity::class.java).apply {}
        startActivity(intent)
        finish()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

    fun addCargs(view:View) {
        val intent = Intent(this, SearchCaregiversActivity::class.java).apply {
        }
        intent.putExtra("pet", myPet)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun volver(view: View) {
        val intent = Intent(this, ViewPetProfileActivity::class.java).apply {
        }
        intent.putExtra("petId", myPet)
        intent.putExtra("eliminat", false)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun save(view: View) {
        myPet.put("birthday", date)
        myPet.put("chip", Integer.valueOf(chipText1.text.toString()))
        myPet.put("nameRace", raceObj)
        if(buttonAddRace.visibility == View.GONE) myPet.put("nameRaceopt", raceObjOpt)
        else myPet.remove("nameRaceopt")
        myPet.save()
        volver(view)
    }

    private fun seeImage() {
        val intent = Intent(this, ImageActivity::class.java).apply {}
        intent.putExtra("object", myPet)
        startActivity(intent)
    }

    private fun deletionDialog(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.pet_deletion_alert_title))
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.delete))
        { dialog, which ->
            deletePet(view)
        }
        builder.setNegativeButton(getString(R.string.cancel))
        { dialog, which -> }
        builder.show()
    }

    override fun updateCaregivers() {
        val caregivers: ParseRelation<ParseUser> = myPet.getRelation<ParseUser>("caregivers")
        val listCaregivers = caregivers.query
        val list = listCaregivers.find()
        if (list != null) {
            viewManager = LinearLayoutManager(this)
            viewAdapter = UserAdapter(list.toList(), owner, myPet, this)
            recyclerView = findViewById<RecyclerView>(R.id.recyclerView1).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                isNestedScrollingEnabled = false

                // use a linear layout manager
                layoutManager = viewManager

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter

            }
        }
    }

}