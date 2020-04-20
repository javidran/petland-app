package com.example.petland.pet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.example.petland.image.ResetImageCallback
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_view_pet_profile.*
import java.text.SimpleDateFormat
import java.util.*

class ViewPetProfileActivity : AppCompatActivity(), ResetImageCallback {

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
        val listUsers = ParseQuery<ParseUser>("_User")
        val powner = myPet.get("owner") as ParseObject
        listUsers.whereEqualTo("objectId", powner.objectId)

        val listCaregivers = caregivers.query

        usernameText.text = myPet.get("name").toString()

        ownerText.text = listUsers.first.username

        raceText.text = "ejemplo1"
        var chipText1 = myPet.get("chip")
        if(chipText1!=null) {
            chipText.setText(chipText1.toString())
        }
        else chipText.setText("")


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
            viewAdapter = UserAdapter(listCaregivers.find(), false)
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
        val imageUtils = ImageUtils()
        imageUtils.retrieveImage(myPet, profileImageView, this)
    }

    fun volver(view: View) {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun edit(view: View) {

        val intent = Intent(this, EditPetProfileActivity::class.java).apply {
        }
        intent.putExtra("petId", myPet);
        startActivity(intent);
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun resetImage() {
        profileImageView.setImageDrawable(this?.getDrawable(R.drawable.animal_paw))
    }

}
