package com.example.petland.pet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.parse.ParseObject
import com.parse.ParseUser
import kotlinx.android.synthetic.main.pet_profile_user_element.view.*


class UserAdapter(
    private val caregivers: List<ParseUser>,
    private val owner: Boolean,
    private val myPet: ParseObject
) :
    RecyclerView.Adapter<UserAdapter.UserHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.pet_profile_user_element, p0, false), this.owner, this.myPet
        )
    }

    override fun getItemCount(): Int {
        return caregivers.count()
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindUserInfo(caregivers[position])
    }

    class UserHolder(v: View, o: Boolean, myPet: ParseObject) : RecyclerView.ViewHolder(v) {
        var view: View = v
        var owner: Boolean = o
        var pet: ParseObject = myPet

        private lateinit var user: ParseUser

        fun bindUserInfo(user: ParseUser) {
            val u = ParseUser.getCurrentUser()
            this.user = user
            view.editPetName.text = user.username
            if (this.owner && u.username != user.username) {
                view.deleteCaregButton.visibility = View.VISIBLE
                view.changeOwnerButton.visibility = View.VISIBLE
                view.deleteCaregButton.setOnClickListener {
                    val relation = pet.getRelation<ParseUser>("caregivers")
                    relation.remove(this.user)
                    pet.saveInBackground()
                }
                view.changeOwnerButton.setOnClickListener { TODO("not implemented") }
            }
            val imageUtils = ImageUtils()
            imageUtils.retrieveImage(user, view.userImage)
        }

    }
}

