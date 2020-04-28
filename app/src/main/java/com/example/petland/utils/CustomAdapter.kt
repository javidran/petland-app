package com.example.petland.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.petland.R

import com.example.petland.image.ImageUtils
import com.parse.ParseObject
import kotlinx.android.synthetic.main.spinner_selection_pet.view.*


class CustomAdapter(
    var context: Context,
    var objectpet: List<ParseObject>,
    var petNames: Array<String>
) :
    BaseAdapter() {
    var inflter: LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return objectpet.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }


    override fun getView(
        i: Int,
        view:  View,
        viewGroup: ViewGroup
    ): View {

         view = inflter.inflate(R.layout.spinner_selection_pet, null)
        val names = view.editPetName
        for (pet in objectpet) {
            val imageUtils = ImageUtils()
            imageUtils.retrieveImage(pet, view.userImage)
            //icon.setImageResource(imageUtils)
        }
        names.text = petNames[i]
        return view
    }

}