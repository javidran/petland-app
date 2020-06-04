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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.image.ImageActivity
import com.example.petland.image.ImageUtils
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_edit_pet_profile.*
import java.text.SimpleDateFormat
import java.util.*


class EditPetProfileActivity : AppCompatActivity(), ViewCaregiversCallback {

    private lateinit var myPet:Pet
    private val TAG = "Petland EditPetProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date
    private lateinit var raceObj: Race
    private lateinit var raceObjOpt: Race
    private lateinit var animalSpecies: AnimalSpecies

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.example.petland.R.layout.activity_edit_pet_profile)
        profileImageView1.setOnClickListener { seeImage() }
        myPet = intent.extras?.get("petId") as Pet
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
        myPet.fetch<Pet>()

        usernameText1.text = myPet.getString("name")
        ownerText1.text = myPet.getOwner().getString("username")

        if(myPet.hasBirthday()) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            date = myPet.getBirthday()
            birthText1.setText(sdf.format(date))
        }
        else {
            birthText1.setText("")
        }

        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date = cal.time
                birthText1.setText(sdf.format(cal.time))
            }

        birthText1.setOnClickListener {
            val dialog = DatePickerDialog(
                this@EditPetProfileActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Date().time
            dialog.show()
        }


        if(myPet.hasChipNumber()) {
            chipText1.setText(myPet.getChipNumber().toString())
        }
        else {
            chipText1.setText("")
        }

        updateCaregivers()

        if (myPet.isOwner(ParseUser.getCurrentUser())) {
            deleteButton.visibility = View.VISIBLE
            addCarg.visibility = View.VISIBLE
        }

        verImagen()
        addElementsToSpinnerRace()
        addElementsToSpinnerRaceOpt()
        if(myPet.hasSecondRace()) {
            buttonAddRace.visibility = View.GONE
            buttonDelRace.visibility = View.VISIBLE
            spinnerRace2.visibility = View.VISIBLE
        }
    }

    private fun addElementsToSpinnerRace() {
        val listRace: ArrayList<String> = ArrayList()
        val myRace = myPet.getFirstRace()
        val objects = Race.getRaces(myPet.getSpecie())
        var i = 0
        var found = false
        for (species in objects) {
            listRace.add(species.getName())
            if(species.objectId == myRace.objectId) found = true
            if(!found) ++i
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
                val query = ParseQuery.getQuery(Race::class.java)
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
        val objects = Race.getRaces(myPet.getSpecie())

        var i = 0
        var found = false
        var myRace : Race? = null
        if(myPet.hasSecondRace()) myRace = myPet.getSecondRace()

        for (species in objects) {
            listRace.add(species.getName())
            if (myRace != null) {
                if (species.objectId == myRace.objectId) found = true
                if (!found) ++i
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
                val query = ParseQuery.getQuery(Race::class.java)
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
        Pet.deletePet(myPet)
        val intent = Intent(this, ViewPetProfileActivity::class.java).apply {}
        intent.putExtra("eliminat", true)
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
        myPet.setBirthday(date)
        myPet.setChipNumber(Integer.valueOf(chipText1.text.toString()))
        myPet.setFirstRace(raceObj)
        if(buttonAddRace.visibility == View.GONE) myPet.setSecondRace(raceObjOpt)
        else myPet.removeSecondRace()
        myPet.savePet()
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
        builder.setPositiveButton(getString(R.string.delete)) { _, _ -> deletePet(view) }
        builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }
        builder.show()
    }

    override fun updateCaregivers() {
        val list = myPet.getCaregivers()
        viewManager = LinearLayoutManager(this)
        viewAdapter = UserAdapter(list, myPet.isOwner(ParseUser.getCurrentUser()), myPet, this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView1).apply {
            isNestedScrollingEnabled = false
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

}