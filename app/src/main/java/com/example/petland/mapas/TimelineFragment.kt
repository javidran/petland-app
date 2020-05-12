package com.example.petland.mapas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.pet.UserAdapter
import com.example.petland.user_profile.PetAdapter
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.fragment_timeline.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

class TimelineFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: TimelineAdapter
    private lateinit var rootView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(this.activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_timeline, container, false)

        val walks = ParseQuery<ParseObject>("Walk").find()
        if (walks != null) {
            recyclerView = rootView.findViewById(R.id.recyclerView5)
            layoutManager = LinearLayoutManager(this.activity)
            recyclerView.layoutManager = layoutManager
            adapter = TimelineAdapter(walks)
            recyclerView.adapter = adapter
        }

        return rootView
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            TimelineFragment().apply {}
    }

}