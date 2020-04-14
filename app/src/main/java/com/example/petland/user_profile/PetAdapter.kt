package com.example.petland.user_profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.image.ImageUtils
import com.parse.ParseObject
import kotlinx.android.synthetic.main.user_profile_pet_element.view.*
import java.text.SimpleDateFormat
import java.util.*

class PetAdapter(private val pets: List<ParseObject>, private val viewPetCallback: ViewPetCallback) :
    RecyclerView.Adapter<PetAdapter.PetHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PetHolder {
        return PetHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.user_profile_pet_element, p0, false),
            viewPetCallback
        )
    }

    override fun getItemCount(): Int {
        return pets.count()
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        holder.bindPetInfo(pets[position])
    }

    class PetHolder(v: View, viewPetCallback: ViewPetCallback) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var view: View = v
        val vpCallback: ViewPetCallback = viewPetCallback

        private lateinit var pet: ParseObject

        init {
            v.setOnClickListener(this)
        }

        fun bindPetInfo(pet: ParseObject) {
            this.pet = pet
            view.name.text = pet.get("name") as String
            view.race.text = "Labrador (raza prueba)"
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            view.birthday.text = sdf.format(pet.get("birthday"))

            val imageUtils = ImageUtils()
            imageUtils.retrieveImage(pet, view.petImage)
        }

        override fun onClick(v: View?) {
            vpCallback.startViewPetActivity(pet)
        }

    }


}