package com.example.petland.mapas

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.timeline_element.view.*
import java.text.SimpleDateFormat
import java.util.*


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

        @SuppressLint("SetTextI18n")
        fun bindWalkHolder(walk: ParseObject) {
            this.walk = walk
            var num = walk.getNumber("duration") as Int
            num = num.div(60000) as Int
            view.durationNum.text = num.toString() + "min"
            view.distanceNum.text = walk.getString("distance") + "km"
            val sdf = SimpleDateFormat("dd/MM/yy HH:mm", Locale.US)
            val fecha = walk.getDate("startDate")

            if (fecha != null) {
                view.hora.text = sdf.format(fecha)
            }

            val users = ParseQuery.getQuery<ParseUser>("_User")
            val pPaseador = walk.get("user") as ParseObject
            users.whereEqualTo("objectId", pPaseador.objectId)
            view.paseadorNom.text = users.first.username
        }

        override fun onClick(v: View?) {
            vwCallback.startViewWalkFragment(walk)
        }

    }
}

