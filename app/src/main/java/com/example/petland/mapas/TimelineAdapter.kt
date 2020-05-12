package com.example.petland.mapas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.parse.ParseObject
import kotlinx.android.synthetic.main.timeline_element.view.*


class TimelineAdapter(
    private val walks: List<ParseObject>
) :
    RecyclerView.Adapter<TimelineAdapter.WalkHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WalkHolder {
        return WalkHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.timeline_element, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return walks.count()
    }

    override fun onBindViewHolder(holder: WalkHolder, position: Int) {
        holder.bindWalkHolder(walks[position])
    }

    class WalkHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view: View = v
        private lateinit var walk: ParseObject

        fun bindWalkHolder(walk: ParseObject) {
            this.walk = walk
            view.duration.text = walk.getNumber("duration").toString()
            view.distance.text = walk.getString("distance")
        }

    }
}

