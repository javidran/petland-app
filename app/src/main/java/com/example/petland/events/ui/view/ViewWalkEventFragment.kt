package com.example.petland.events.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.model.WalkEvent

class ViewWalkEventFragment : Fragment() {
    private lateinit var event: WalkEvent
    private lateinit var rootView: View

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

        return rootView
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
