package com.example.petland.user_profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.image.ImageUtils
import com.example.petland.R
import com.parse.ParseObject
import kotlinx.android.synthetic.main.user_profile_pet_element.view.*
import java.text.SimpleDateFormat
import java.util.*

private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)

class PetAdapter(private val pets: List<ParseObject>) : RecyclerView.Adapter<PetAdapter.PetHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PetHolder {
        return PetHolder(LayoutInflater.from(p0.context).inflate(R.layout.user_profile_pet_element, p0, false))
    }

    override fun getItemCount(): Int {
        return pets.count()
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        holder.bindPetInfo(pets[position])
    }

    class PetHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener  {

        var view : View = v
        private lateinit var pet : ParseObject

        init {
            v.setOnClickListener(this)
        }

        fun bindPetInfo(pet :ParseObject) {
            this.pet = pet
            view.name.text = pet.get("name") as String
            view.race.text = "Labrador (raza prueba)"
            view.birthday.text = sdf.format(pet.get("birthday"))

            val imageUtils = ImageUtils()
            imageUtils.retrieveImage(pet, view.petImage)
        }

        override fun onClick(v: View?) {
            Toast.makeText(view.context, "Funcionalidad no disponible", Toast.LENGTH_SHORT).show()
        }

    }




}