package com.example.petland.pet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.image.ImageActivity
import com.example.petland.image.ImageUtils
import com.example.petland.image.ResetImageCallback
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_view_pet_profile.*
import kotlinx.android.synthetic.main.activity_view_pet_profile.profileImage
import kotlinx.android.synthetic.main.activity_view_pet_profile.textViewName
import kotlinx.android.synthetic.main.activity_view_pet_profile.textViewOwner
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import java.text.SimpleDateFormat
import java.util.*

class ViewPetProfileActivity : AppCompatActivity(), ResetImageCallback {

    private lateinit var myPet:ParseObject
    private val TAG = "Petland EditPetProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private lateinit var rootView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pet_profile)
        setData()
    }

    private fun setData() {
        myPet = intent.extras?.get("petId") as ParseObject
        val caregivers: ParseRelation<ParseUser> = myPet.getRelation<ParseUser>("caregivers")
        val listCaregivers = caregivers.query

        textViewName.text = myPet.get("name").toString()

        val dateb = sdf.format(myPet.get("birthday"))
        textViewBirth.text = dateb.toString()

        textViewChip.text = myPet.get("chip").toString()
        textViewOwner.text = listCaregivers.first.get("username").toString()

        val list = findViewById<TextView>(R.id.petCaregiversList)
        list.text = ""
        listCaregivers.findInBackground { result, e ->
            if (e == null) {
                for(el in result) {
                    list.text = (list.text as String).plus("- ".plus(el.username).plus("\n"))
                }
            } else {
                Log.d(TAG, "PetQuery not completed")
            }
        }

        val imageUtils = ImageUtils()
        imageUtils.retrieveImage(myPet, profileImage, this)
    }

    fun volver(view: View) {
        val intent = Intent(this, HomeActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun editPetProfile(view: View) {

        val intent = Intent(this, EditPetProfileActivity::class.java).apply {
        }
        intent.putExtra("petId", myPet);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun resetImage() {
        profileImage.setImageDrawable(this?.getDrawable(R.drawable.animal_paw))
    }

}