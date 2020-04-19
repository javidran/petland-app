package com.example.petland.events.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.example.petland.R
import com.example.petland.events.enums.EventType
import com.example.petland.events.model.*
import com.parse.ParseObject
import kotlinx.android.synthetic.main.activity_create_event.*
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

class CreateEventActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var myPet: ParseObject
    lateinit var event: PetEvent
    lateinit var data : ParseObject

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    private val stf = SimpleDateFormat("HH:mm", Locale.US)
    private val resultdf = SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        myPet = intent.extras?.get("petId") as ParseObject
        event = PetEvent()

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
                            val date: Date = resultdf.parse(untilDateDay.text as String + untilDateHour.text as String)
                                    ?: throw NullPointerException("Date is wrong")
                            event.setRecurrencyEndDate(date)
                        }
                    } else {
                        event.removeRecurrencyEndDate()
                    }
                    event.setRecurrent(recurrencyNumber.text.toString().toInt())
                }
            } else {
                event.removeRecurrency()
            }

            //TODO: Conseguir data
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
        when (parent.getItemAtPosition(pos).toString()) {
            getString(R.string.vaccine) -> {
                data = VaccineEvent()
                typeLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.typeLayout, CreateVaccineEventFragment.newInstance(data as VaccineEvent))
                transaction.commit()
            }
            getString(R.string.food) -> {
                data = FoodEvent()
                typeLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            }
            getString(R.string.hygiene) -> {
                data = HygieneEvent()
                typeLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            }
            getString(R.string.measurement) -> {
                data = MeasurementEvent()
                typeLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            }
            getString(R.string.medicine) -> {
                data = MedicineEvent()
                typeLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            }
            getString(R.string.walk) -> {
                data = WalkEvent()
                typeLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            }

        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

}
