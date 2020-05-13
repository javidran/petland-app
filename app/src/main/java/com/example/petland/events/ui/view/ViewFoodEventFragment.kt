package com.example.petland.events.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.enums.FoodType
import com.example.petland.events.model.FoodEvent
import kotlinx.android.synthetic.main.fragment_view_food_event.view.*

class ViewFoodEventFragment : Fragment() {
    private lateinit var event: FoodEvent
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
        rootView = inflater.inflate(R.layout.fragment_view_food_event, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()

        val dataTypeString: String = when (event.getType()) {
            FoodType.MEAT -> context!!.getString(R.string.food_meat)
            FoodType.TOOTH_BARS -> context!!.getString(R.string.food_tooth_bars)
            FoodType.WATER -> context!!.getString(R.string.food_water)
            FoodType.FEED -> context!!.getString(R.string.food_water)
        }
        rootView.viewFoodType.text = dataTypeString

        rootView.viewFoodAmount.text = event.getAmount().toString()

        if(event.hasInfo()) {
            rootView.viewFoodInfoLayout.visibility = View.VISIBLE
            rootView.viewFoodInfo.text = event.getInfo()
        } else {
            rootView.viewFoodInfoLayout.visibility = View.GONE
        }
    }


    companion object {
        private const val ARG_EVENT_DATA = "arg_event_data"

        @JvmStatic
        fun newInstance(event: FoodEvent) =
            ViewFoodEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_EVENT_DATA, event)
                }
            }
    }
}
