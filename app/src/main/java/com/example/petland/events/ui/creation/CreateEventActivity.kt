package com.example.petland.events.ui.creation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petland.R
import com.example.petland.events.enums.EventType
import com.example.petland.events.model.PetEvent
import com.parse.ParseObject
import kotlinx.android.synthetic.main.activity_create_event.*
import java.text.SimpleDateFormat
import java.util.*

class CreateEventActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var myPet: ParseObject
    lateinit var event: PetEvent
    lateinit var callback: SaveDataCallback

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    private val stf = SimpleDateFormat("HH:mm", Locale.US)
    private val resultdf = SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        myPet = intent.extras?.get("petId") as ParseObject
        event = PetEvent()
        event.setPet(myPet)

        returnButton.setOnClickListener { goBack() }
        saveButton.setOnClickListener { onSaveButtonClicked() }
        setDateChooser(dateDay)
        setDateChooser(untilDateDay)
        setHourChooser(dateHour)
        setHourChooser(untilDateHour)
        setCheckBoxes()

        val spinner: Spinner = findViewById(R.id.typeSpinner)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, getEventTypeNames()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

    }

    fun goBack() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun setDateChooser(textView : TextView) {
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                textView.text = sdf.format(cal.time)
            }

        textView.setOnClickListener {
            val dialog = DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }

    }

    private fun setHourChooser(textView : TextView) {
        val cal = Calendar.getInstance()

        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                textView.text = stf.format(cal.time)
            }

        textView.setOnClickListener {
            val dialog = TimePickerDialog(
                this, timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            )
            dialog.show()
        }
    }

    private fun setCheckBoxes() {
        recurrencyLayout.visibility = View.GONE
        recurrencyCheckbox.setOnCheckedChangeListener { _, b ->
            if(b) recurrencyLayout.visibility = View.VISIBLE
            else recurrencyLayout.visibility = View.GONE
        }

        untilDateDay.visibility = View.GONE
        untilDateHour.visibility = View.GONE
        recurrencyUntilCheckbox.setOnCheckedChangeListener { _, b ->
            if(b) {
                untilDateDay.visibility = View.VISIBLE
                untilDateHour.visibility = View.VISIBLE
            }
            else {
                untilDateDay.visibility = View.GONE
                untilDateHour.visibility = View.GONE
            }
        }
    }

    private fun onSaveButtonClicked() {
        if(dateDay.text.isEmpty()) dateDay.error = "Date needed"
        else {
            if (recurrencyCheckbox.isChecked) {
                if (recurrencyNumber.text.isEmpty()) recurrencyNumber.error = "Date needed"
                else {
                    if (recurrencyUntilCheckbox.isChecked) {
                        if (untilDateDay.text.isEmpty()) untilDateDay.error = "Date needed"
                        else {
                            val dateEnd: Date = resultdf.parse(untilDateDay.text as String + untilDateHour.text as String)
                                    ?: throw NullPointerException("Date is wrong")
                            event.setRecurrencyEndDate(dateEnd)
                        }
                    } else {
                        event.removeRecurrencyEndDate()
                    }
                    event.setRecurrent(recurrencyNumber.text.toString().toInt())
                }
            } else {
                event.removeRecurrency()
            }
            val date: Date = resultdf.parse(dateDay.text as String + dateHour.text as String)
                ?: throw NullPointerException("Date is wrong")
            event.setDate(date)
            val data = callback.checkAndSaveData()
            if(data != null) {
                event.setData(data)
                event.saveEvent()
                finish()
            }
        }
    }


    private fun getEventTypeNames() : Array<String?> {
        val array = arrayOfNulls<String>(EventType.values().size)
        EventType.values().forEachIndexed { it, el ->
            when (el) {
                EventType.VACCINE -> array[it] = getString(R.string.vaccine)
                EventType.FOOD -> array[it] = getString(R.string.food)
                EventType.HYGIENE -> array[it] = getString(R.string.hygiene)
                EventType.MEASUREMENT -> array[it] = getString(R.string.measurement)
                EventType.MEDICINE -> array[it] = getString(R.string.medicine)
                EventType.WALK -> array[it] = getString(R.string.walk)
            }
        }
        return array
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        typeLayout.removeAllViews()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        lateinit var fragment: Fragment
        when (parent.getItemAtPosition(pos).toString()) {
            getString(R.string.vaccine) -> {
                fragment = CreateVaccineEventFragment.newInstance()
                callback = fragment
            }
            getString(R.string.food) -> {
                fragment = CreateFoodEventFragment.newInstance()
                callback = fragment
            }
            getString(R.string.hygiene) -> {
                fragment = CreateHygieneEventFragment.newInstance()
                callback = fragment
            }
            getString(R.string.measurement) -> {
                fragment = CreateMeasurementEventFragment.newInstance()
                callback = fragment
            }
            getString(R.string.medicine) -> {
                fragment = CreateMedicineEventFragment.newInstance()
                callback = fragment
            }
            getString(R.string.walk) -> {
                fragment = CreateWalkEventFragment.newInstance()
                callback = fragment
            }
        }
        transaction.replace(R.id.typeLayout, fragment)
        transaction.commit()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

}
