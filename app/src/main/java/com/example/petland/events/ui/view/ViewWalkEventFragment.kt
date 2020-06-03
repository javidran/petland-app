package com.example.petland.events.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.WalkEvent
import com.example.petland.mapas.ViewWalksActivity
import com.example.petland.mapas.Walk
import kotlinx.android.synthetic.main.fragment_view_walk_event.*
import kotlinx.android.synthetic.main.fragment_view_walk_event.view.buttonViewMap

class ViewWalkEventFragment : Fragment() {
    private lateinit var event: WalkEvent
    private lateinit var rootView: View
    private lateinit var walk : Walk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            event = it.getParcelable(ARG_EVENT_DATA)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_view_walk_event, container, false)
        rootView.buttonViewMap.setOnClickListener { startViewWalkFragment() }

        return rootView
    }


    override fun onResume() {
        super.onResume()
      setWalkInfo()
    }
    fun setWalkInfo() {
        if (event.hasWalk()){
            walk = event.getWalk()
            textWalkTime.text =  getString(R.string.time) + " " + (walk.getTime()/60000).toString() + " min."
            textWalkKm.text = getString(R.string.distance_value)+ " "  + walk.getRecorrido() + " km."
            buttonViewMap.visibility = VISIBLE
            textWalkKm.visibility = VISIBLE
            textWalkTime.visibility = VISIBLE
            textwalkAssigned.visibility = VISIBLE
        }
        else {
            buttonViewMap.visibility = INVISIBLE
            textWalkKm.visibility = INVISIBLE
            textWalkTime.visibility = INVISIBLE
            textwalkAssigned.visibility = INVISIBLE
        }
    }
     fun startViewWalkFragment() {
        val intent = Intent(context, ViewWalksActivity::class.java).apply {}
        intent.putExtra("walk", walk)
        startActivity(intent);
    }

    companion object {
        private const val ARG_EVENT_DATA = "arg_event_data"

        @JvmStatic
        fun newInstance(event: WalkEvent) =
            ViewWalkEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EVENT_DATA, event)
                }
            }
    }
}
