package com.example.petland.pet

import Race
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_view_pet_profile.*
import java.text.SimpleDateFormat
import java.util.*

class ViewPetProfileActivity : AppCompatActivity() , ViewCaregiversCallback{

    private lateinit var myPet:ParseObject
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
        myPet = intent.extras?.get("petId") as ParseObject
        val caregivers: ParseRelation<ParseUser> = myPet.getRelation<ParseUser>("caregivers")
        val listCaregivers = caregivers.query
        val list = listCaregivers.find()

        val listUsers = ParseQuery.getQuery<ParseUser>("_User")
        val powner = myPet.get("owner") as ParseObject
        listUsers.whereEqualTo("objectId", powner.objectId)

        writeRace()

        usernameText.text = myPet.get("name").toString()
        ownerText.text = listUsers.first.username

        var chipText1 = myPet.get("chip")
        if(chipText1!=null) {
            chipText.text = chipText1.toString()
        }
        else chipText.text = ""


        val dateb = myPet.get("birthday")
        if(dateb!=null) {
            birthText.visibility = View.VISIBLE
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            birthText.text = sdf.format(dateb)
        }
        else {
            birthText.visibility = View.GONE
        }

        if (listCaregivers != null) {
            viewManager = LinearLayoutManager(this)
            viewAdapter = UserAdapter(list.toList(), false, myPet, this)
            recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = viewManager

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter

            }
        }
        ImageUtils.retrieveImage(myPet, profileImageView)
    }

    fun writeRace() {
        val listRaces = ParseQuery.getQuery(Race::class.java)

        val pRacePrincipal = myPet.get("nameRace") as ParseObject
        val racePrincipal = listRaces
        racePrincipal.whereEqualTo("objectId", pRacePrincipal.objectId)

        raceText.text = racePrincipal.first.getName()

        val pRaceSecundaria:ParseObject? = myPet.getParseObject("nameRaceopt")
        if(pRaceSecundaria != null) {
            val raceSecundaria = listRaces
            raceSecundaria.whereEqualTo("objectId", pRaceSecundaria.objectId)
            raceTextSecond.text = racePrincipal.first.getName()
        }

    }

    fun volver(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun edit(view: View) {

        val intent = Intent(this, EditPetProfileActivity::class.java).apply {
        }
        intent.putExtra("petId", myPet)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun updateCaregivers() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}