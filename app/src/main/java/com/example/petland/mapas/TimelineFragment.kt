package com.example.petland.mapas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.pet.Pet
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.fragment_timeline.view.*

class TimelineFragment : Fragment(), ViewWalkCallback {
    private lateinit var recyclerView: RecyclerView

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: TimelineAdapter
    private lateinit var rootView: View
    lateinit var fragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_timeline, container, false)

        rootView.recyclerViewTimeline.isNestedScrollingEnabled = false
        rootView.recyclerViewTimeline.layoutManager = layoutManager
        updatePaseos()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        updatePaseos()
    }

    private fun updatePaseos() {
        this.adapter = TimelineAdapter(Walk.getWalksFromPet(Pet.getSelectedPet()), this)
        rootView.recyclerViewTimeline.adapter = adapter
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            TimelineFragment().apply {}
    }

    override fun startViewWalkFragment(walk: ParseObject) {
        val intent = Intent(context, ViewWalksActivity::class.java).apply {}
        intent.putExtra("walk", walk)
        startActivity(intent);
    }

}