package com.example.petland.pet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import kotlinx.android.synthetic.main.activity_view_pet_profile.*
import java.text.SimpleDateFormat
import java.util.*

class ViewPetProfileActivity : AppCompatActivity() {

    private lateinit var myPet: Pet
    private val TAG = "Petland EditPetProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pet_profile)
        if (intent.extras?.get("eliminat") as Boolean) finish()
        else setData()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        myPet = intent.extras?.get("petId") as Pet
        writeRace()

        usernameText.text = myPet.getName()
        ownerText.text = myPet.getOwner().username

        if(myPet.hasChipNumber()) {
            chipText.text = myPet.getChipNumber().toString()
        }
        else {
            chipText.text = ""
        }

        if(myPet.hasBirthday()) {
            birthText.visibility = View.VISIBLE
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            birthText.text = sdf.format(myPet.getBirthday())
        }
        else {
            birthText.visibility = View.GONE
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = UserAdapter(myPet.getCaregivers(), false, myPet, null)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            isNestedScrollingEnabled = false
            layoutManager = viewManager
            adapter = viewAdapter
        }

        ImageUtils.retrieveImage(myPet, profileImageView)
    }

    fun writeRace() {
        raceText.text = myPet.getFirstRace().getName()
        if(myPet.hasSecondRace()) {
            raceTextSecond.text = myPet.getSecondRace().getName()
        }

    }

    fun volver(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun edit(view: View) {
        val intent = Intent(this, EditPetProfileActivity::class.java).apply {}
        intent.putExtra("petId", myPet)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}