package com.example.petland.pet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.user_profile_pet_element.view.name
import kotlinx.android.synthetic.main.pet_profile_user_element.view.*

class CaregiversAdapter(
    private val caregivers: List<ParseUser>
) :
    RecyclerView.Adapter<CaregiversAdapter.UserHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.user_profile_caregiver_element, p0, false))
    }

    override fun getItemCount(): Int {
        return caregivers.count()
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindUserInfo(caregivers[position])
    }

    class UserHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener{
        var view: View = v

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
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}