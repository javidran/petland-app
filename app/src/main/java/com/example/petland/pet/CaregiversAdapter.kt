package com.example.petland.pet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.example.petland.user_profile.invitations.Invitation
import com.parse.Parse.getApplicationContext
import com.parse.ParseUser
import kotlinx.android.synthetic.main.user_profile_caregiver_element.view.*


class CaregiversAdapter(
    private val caregivers: List<ParseUser>,
    private val myPet: Pet
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

    class UserHolder(v: View, myPet: Pet) : RecyclerView.ViewHolder(v), View.OnClickListener{
        var view: View = v
        var pet: Pet = myPet

        private lateinit var user: ParseUser

        init {
            v.setOnClickListener(this)
        }

        fun bindUserInfo(user: ParseUser) {
            this.user = user
            view.name.text = user.username
            ImageUtils.retrieveImage(user, view.userImage)
        }

        override fun onClick(v: View?) {
            val cUser = ParseUser.getCurrentUser()

            if (Invitation.existsInvitation(cUser, this.user, pet)) {
                Toast.makeText(
                    getApplicationContext(),
                    getApplicationContext().getString(R.string.invi_exists), Toast.LENGTH_SHORT
                ).show()
            }
            else {
                Invitation.createInvitation(cUser, this.user, pet)
                Toast.makeText(
                    getApplicationContext(),
                    getApplicationContext().getString(R.string.invi_sent), Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}