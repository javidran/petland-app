package com.example.petland.pet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.parse.Parse.getApplicationContext
import com.parse.ParseObject
import com.parse.ParseUser
import kotlinx.android.synthetic.main.pet_profile_user_element.view.*
import kotlinx.android.synthetic.main.user_profile_pet_element.view.name


class CaregiversAdapter(
    private val caregivers: List<ParseUser>,
    private val myPet: ParseObject
) :
    RecyclerView.Adapter<CaregiversAdapter.UserHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.user_profile_caregiver_element, p0, false), this.myPet)
    }

    override fun getItemCount(): Int {
        return caregivers.count()
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindUserInfo(caregivers[position])
    }

    class UserHolder(v: View, myPet: ParseObject) : RecyclerView.ViewHolder(v), View.OnClickListener{
        var view: View = v
        var pet:ParseObject = myPet

        private lateinit var user: ParseUser

        init {
            v.setOnClickListener(this)
        }

        fun bindUserInfo(user: ParseUser) {
            this.user = user
            view.name.text = user.username
            val imageUtils = ImageUtils()
            imageUtils.retrieveImage(user, view.userImage)
        }

        override fun onClick(v: View?) {
            val cUser = ParseUser.getCurrentUser()
            val invitation = ParseObject("Invitation")

            invitation.put("creator", cUser )
            invitation.put("receiver", this.user)
            invitation.put("petO", pet)
            invitation.saveInBackground()

            val toast1 = Toast.makeText(
                getApplicationContext(),
                "Solicitud enviada", Toast.LENGTH_SHORT
            )

            toast1.show()
        }
    }
}