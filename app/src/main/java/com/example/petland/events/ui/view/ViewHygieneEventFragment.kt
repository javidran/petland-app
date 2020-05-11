package com.example.petland.events.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.enums.HygieneType
import com.example.petland.events.model.HygieneEvent
import kotlinx.android.synthetic.main.fragment_view_hygiene_event.view.*

class ViewHygieneEventFragment : Fragment() {
    private lateinit var event: HygieneEvent
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
        rootView = inflater.inflate(R.layout.fragment_view_hygiene_event, container, false)

        return rootView
    }

    override fun onResume() {
        super.onResume()

        val dataTypeString: String = when (event.getType()) {
            HygieneType.NAILS -> context!!.getString(R.string.hygiene_nails)
            HygieneType.HAIRCUT -> context!!.getString(R.string.hygiene_haircut)
            HygieneType.DEWORM -> context!!.getString(R.string.hygiene_deworm)
            HygieneType.BATH -> context!!.getString(R.string.hygiene_bath)
        }
        rootView.viewHygieneType.text = dataTypeString
    }


    companion object {
        private const val ARG_EVENT_DATA = "arg_event_data"

        @JvmStatic
        fun newInstance(event: HygieneEvent) =
            ViewHygieneEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EVENT_DATA, event)
                }
            }
    }
}
