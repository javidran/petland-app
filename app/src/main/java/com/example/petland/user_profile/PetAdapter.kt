package com.example.petland.user_profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.parse.ParseObject
import kotlinx.android.synthetic.main.user_profile_pet_element.view.*
import java.text.SimpleDateFormat
import java.util.*

class PetAdapter(private val pets: List<ParseObject>) : RecyclerView.Adapter<PetAdapter.PetHolder>(){
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PetHolder {
        return PetHolder(LayoutInflater.from(p0.context).inflate(R.layout.user_profile_pet_element, p0, false))
    }

    override fun getItemCount(): Int {
        return pets.count()
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        val pet = pets.get(position)
        val name = pet.get("name").toString()
        val race = "Labrador (raza prueba)"
        val birthday = sdf.format(pet.get("birthday"))
        holder.bindPetInfo(name, race, birthday)
    }

    class PetHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener  {

        var view : View = v
        var name : String = ""
        var race : String = ""
        var birthday : String = ""

        init {
            v.setOnClickListener(this)
        }

        fun bindPetInfo(name:String, race:String, birthday:String) {
            this.name = name
            this.race = race
            this.birthday = birthday
            view.name.text = name
            view.race.text = race
            view.birthday.text= birthday
        }

        override fun onClick(v: View?) {
            Toast.makeText(view.context, "Funcionalidad no disponible", Toast.LENGTH_SHORT).show()
        }

    }




}