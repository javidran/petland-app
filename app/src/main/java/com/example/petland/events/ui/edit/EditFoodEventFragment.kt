package com.example.petland.events.ui.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.events.enums.FoodType
import com.example.petland.events.model.FoodEvent
import com.example.petland.events.ui.callback.SaveDataCallback
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_edit_food_event.view.*
import kotlinx.android.synthetic.main.fragment_edit_medicine_event.view.*

class EditFoodEventFragment : Fragment(),
    SaveDataCallback {
    private var dataEvent = FoodEvent()
    private lateinit var rootView: View
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataEvent = it.getParcelable(ARG_PARAM)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_edit_food_event, container, false)

        spinner = rootView.foodSpinner
        val con: Context = context ?: throw NullPointerException()
        val adapter = ArrayAdapter(
            con,
            android.R.layout.simple_spinner_item, getEventFoodToString()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val dataType = dataEvent.getType()
        FoodType.values().forEachIndexed { index, foodType ->
            if(foodType == dataType) {
                spinner.setSelection(index)
            }
        }

        rootView.editFoodAmount.setText(dataEvent.getAmount().toString())
        if(dataEvent.hasInfo()) {
            rootView.editFoodInfo.setText(dataEvent.getInfo())
        }

        return rootView
    }

    override fun checkAndSaveData(): ParseObject? {
        if(rootView.editFoodAmount.text.isEmpty()) {
            rootView.editFoodAmount.error = getString(R.string.needed)
            return null
        }
        else {
            dataEvent.setAmount(rootView.editFoodAmount.text.toString().toInt())
            if(rootView.editFoodInfo.text.isNotEmpty()) {
                dataEvent.setInfo(rootView.editFoodInfo.text.toString())
            }
            dataEvent.setType(getEventFoodToEnum(spinner.selectedItem.toString()))
            dataEvent.saveEvent()
            return dataEvent
        }

    }

    private fun getEventFoodToString() : Array<String?> {
        val array = arrayOfNulls<String>(FoodType.values().size)
        FoodType.values().forEachIndexed { it, el ->
            when (el) {
                FoodType.FEED -> array[it] = getString(R.string.food_feed)
                FoodType.MEAT -> array[it] = getString(R.string.food_meat)
                FoodType.TOOTH_BARS -> array[it] = getString(R.string.food_tooth_bars)
                FoodType.WATER -> array[it] = getString(R.string.food_water)
            }
        }
        return array
    }

    private fun getEventFoodToEnum(value: String) : FoodType {
        when (value) {
            getString(R.string.food_feed) -> return FoodType.FEED
            getString(R.string.food_meat) -> return FoodType.MEAT
            getString(R.string.food_tooth_bars) -> return FoodType.FEED
            getString(R.string.food_water) -> return FoodType.WATER
        }
        return FoodType.MEAT //Default
    }

    companion object {
        private const val TAG = "Petland Events"
        private const val ARG_PARAM = "arg_param"
        const val DATA_EVENT = "data_event"

        @JvmStatic
        fun newInstance(dataEvent: FoodEvent) =
            EditFoodEventFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM, dataEvent)
                }
            }
    }
}
