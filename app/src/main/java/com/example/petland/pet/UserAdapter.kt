package com.example.petland.pet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.parse.ParseUser
import kotlinx.android.synthetic.main.user_profile_pet_element.view.name
import kotlinx.android.synthetic.main.pet_profile_user_element.view.*

class UserAdapter(
    private val caregivers: List<ParseUser>,
    private val owner: Boolean
) :
    RecyclerView.Adapter<UserAdapter.UserHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.pet_profile_user_element, p0, false), this.owner
        )
    }

    override fun getItemCount(): Int {
        return caregivers.count()
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindPetInfo(caregivers[position])
    }

    class UserHolder(v: View, o: Boolean) : RecyclerView.ViewHolder(v) {
        var view: View = v
        var owner: Boolean = o

        private lateinit var user: ParseUser

        fun bindPetInfo(user: ParseUser) {
            val u = ParseUser.getCurrentUser()
            this.user = user
            view.name.text = user.username
            if (this.owner && u.username != user.username) {
                view.deleteCaregButton.visibility = View.VISIBLE
                view.changeOwnerButton.visibility = View.VISIBLE
            }
            val imageUtils = ImageUtils()
            imageUtils.retrieveImage(user, view.userImage)
        }
    }


}