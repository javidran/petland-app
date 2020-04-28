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
    private var objectpet: List<ParseObject>,
    private var petNames: Array<String>
) :
    BaseAdapter() {
    var inflter: LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return objectpet.size
    }

    override fun getItem(i: Int): ParseObject {
        return objectpet[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }


    override fun getView(
        i: Int,
        view:  View?,
        viewGroup: ViewGroup
    ): View {

        val viewpet: View = inflter.inflate(R.layout.spinner_selection_pet, null)
        val names = viewpet.editPetName

        ImageUtils.retrieveImage(objectpet[i], viewpet.userImage)
        names.text = petNames[i]
        return viewpet
    }

}