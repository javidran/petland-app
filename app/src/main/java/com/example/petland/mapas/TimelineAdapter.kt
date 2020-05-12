package com.example.petland.mapas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.parse.ParseObject
import kotlinx.android.synthetic.main.timeline_element.view.*


class TimelineAdapter(
    private val walks: List<ParseObject>,
    private val viewWalkCallback: ViewWalkCallback
) :
    RecyclerView.Adapter<TimelineAdapter.WalkHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WalkHolder {
        return WalkHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.timeline_element, p0, false), viewWalkCallback
        )
    }

    override fun getItemCount(): Int {
        return walks.count()
    }

    override fun onBindViewHolder(holder: WalkHolder, position: Int) {
        holder.bindWalkHolder(walks[position])
    }

    class WalkHolder(v: View, viewWalkCallback: ViewWalkCallback) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var view: View = v
        private lateinit var walk: ParseObject

        private val vwCallback: ViewWalkCallback = viewWalkCallback

        init {
            v.setOnClickListener(this)
        }

        fun bindWalkHolder(walk: ParseObject) {
            this.walk = walk
            view.durationNum.text = walk.getNumber("duration").toString()
            view.distanceNum.text = walk.getString("distance")
        }

        override fun onClick(v: View?) {
            vwCallback.startViewWalkFragment(walk)
        }

    }
}

