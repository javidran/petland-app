package com.example.petland.pet

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageActivity
import com.example.petland.image.ImageUtils
import com.example.petland.image.ResetImageCallback
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_edit_pet_profile.*
import java.text.SimpleDateFormat
import java.util.*

class EditPetProfileActivity : AppCompatActivity(), ResetImageCallback {

    private lateinit var myPet:ParseObject
    private val TAG = "Petland EditPetProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pet_profile)
        profileImageView1.setOnClickListener { seeImage() }
        myPet = intent.extras?.get("petId") as ParseObject
        setData()
    }

    override fun onResume() {
        super.onResume()
        setData()
        verImagen()
    }

    private fun verImagen () {
        val imageUtils1 = ImageUtils()
        imageUtils1.retrieveImage(myPet, profileImageView1, this)
    }

    private fun setData() {

        val user = ParseUser.getCurrentUser()
        myPet.fetch<ParseObject>()
        val caregivers: ParseRelation<ParseUser> = myPet.getRelation<ParseUser>("caregivers")
        val listCaregivers = caregivers.query

        val listUsers = ParseQuery<ParseUser>("_User")
        val powner = myPet.get("owner") as ParseObject
        listUsers.whereEqualTo("objectId", powner.objectId)

        usernameText1.text = myPet.getString("name")
        ownerText1.text = listCaregivers.first.get("username").toString()


        val textViewBirthday: TextView = findViewById(R.id.birthText1)
        val cal = Calendar.getInstance()
        var birth = myPet.get("birthday")
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


        var chipText = myPet.get("chip")
        if(chipText!=null) {
            chipText1.setText(chipText.toString())
        }
        else chipText1.setText("")

        if (ownerText1.text == user.username) {     // enable buttons
            val deleteButton:TextView = findViewById(R.id.deleteButton)
            deleteButton.visibility = View.VISIBLE
            val addCarg:TextView = findViewById(R.id.addCarg)
            addCarg.visibility = View.VISIBLE
            val viewCargs:RecyclerView = findViewById(R.id.recyclerView1)
            viewCargs.visibility = View.VISIBLE
            if (listCaregivers != null) {
                viewManager = LinearLayoutManager(this)
                viewAdapter = UserAdapter(listCaregivers.find(), true)
                recyclerView = findViewById<RecyclerView>(R.id.recyclerView1).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)

                    // use a linear layout manager
                    layoutManager = viewManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter

                }
            }
        }
        verImagen()
    }

    fun wantToDeletePet(view: View) {
        deletionDialog(view)
    }

    fun deletePet(view: View) {
        //
        myPet.deleteInBackground { e ->
            if (e == null) {
                Log.d(TAG, "Pet correctly deleted!") //Mensaje en logcat
            } else {
                Log.d(TAG, "An error occurred while deleting a pet!") //Mensaje en logcat
            }
        }
        val intent = Intent(this, ViewPetProfileActivity::class.java).apply {
        }
        intent.putExtra("eliminat", true)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

    }

    fun deleteCare(view:View) {
        // Codi per eliminar un cuidador aqui
    }

    fun changeOwner(view:View) {
        // Codi per canviar d'amo aqui
    }

    fun addCargs(view:View) {
        // Codi per afegir un cuidador aqui
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

    fun edit(view: View) {
        if(!TextUtils.isEmpty(birthText1.text)) myPet.put("birthday", date)
        if(!TextUtils.isEmpty(chipText1.text)) myPet.put("chip", Integer.valueOf(chipText1.text.toString()))
        myPet.save()
        volver(view)
    }

    private fun seeImage() {
        val intent = Intent(this, ImageActivity::class.java).apply {}
        intent.putExtra("object", myPet)
        startActivity(intent)
    }

    override fun resetImage() {
        profileImageView1.setImageDrawable(this?.getDrawable(R.drawable.animal_paw))
    }

    private fun deletionDialog(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.deletionAlertTitle))
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.delete))
        { dialog, which ->
            deletePet(view)
        }
        builder.setNegativeButton(getString(R.string.cancel))
        { dialog, which -> }
        builder.show()
    }

}