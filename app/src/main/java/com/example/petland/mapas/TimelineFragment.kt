package com.example.petland.mapas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.pet.Pets
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
        val pet = Pets.getSelectedPet()
        val walks = ParseQuery<ParseObject>("Walk")
        walks.whereEqualTo("pet", pet)
        val list = walks.find()

        this.adapter = TimelineAdapter(list.toList(), this)
        rootView.recyclerViewTimeline.adapter = adapter
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            TimelineFragment().apply {}
    }

    override fun startViewWalkFragment(walk: ParseObject) {
        val bundle = Bundle()
        bundle.putString("objectId", walk.objectId)
        val transaction = requireFragmentManager().beginTransaction()
        fragment = ViewWalksFragment.newInstance()
        fragment.arguments = bundle
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

}