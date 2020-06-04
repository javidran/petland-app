package com.example.petland.events.ui.edit

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petland.R
import com.example.petland.events.enums.EventType
import com.example.petland.events.model.*
import com.example.petland.events.ui.callback.SaveDataCallback
import com.example.petland.events.ui.creation.*
import com.example.petland.events.ui.view.ViewEventActivity
import com.example.petland.pet.Pet
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_edit_event.*
import java.text.SimpleDateFormat
import java.util.*

class EditEventActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var petList: List<Pet>
    private lateinit var caregiversList: List<ParseUser>
    private lateinit var event: PetEvent
    private lateinit var callback: SaveDataCallback

    private lateinit var spinnerEventType: Spinner
    private lateinit var spinnerPet: Spinner
    private lateinit var spinnerAssigned: Spinner

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    private val stf = SimpleDateFormat("HH:mm", Locale.US)
    private val resultdf = SimpleDateFormat("dd/MM/yyyyHH:mm", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        event = intent.extras?.get("event") as PetEvent

        returnButton.setOnClickListener { goBack() }
        deleteEventButton.setOnClickListener { deletionDialog() }
        saveButton.setOnClickListener { onSaveButtonClicked() }
        setDateChooser(dateDay)
        setDateChooser(untilDateDay)
        setHourChooser(dateHour)
        setHourChooser(untilDateHour)
        setCheckBoxes()

        dateDay.text = sdf.format(event.getDate())
        dateHour.text = stf.format(event.getDate())

        spinnerEventType = findViewById(R.id.typeSpinner)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, getEventTypeNames()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEventType.adapter = adapter
        spinnerEventType.onItemSelectedListener = this
        val dataType = event.getDataType()
        EventType.values().forEachIndexed { index, eventType ->
            if(eventType == dataType) {
                spinnerEventType.setSelection(index, true)
            }
        }

        petList = Pet.getPetsFromCurrentUser()

        spinnerPet = findViewById(R.id.spinnerPet)
        val adapterPet = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, Pet.getNamesFromPetList(petList)
        )
        adapterPet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPet.adapter = adapterPet
        spinnerPet.onItemSelectedListener = this

        val eventPet = event.getPet()
        petList.forEachIndexed { index, parseObject ->
            if(parseObject.objectId == eventPet.objectId) {
                spinnerPet.setSelection(index, true)
            }
        }

        spinnerAssigned = findViewById(R.id.assignedSpinner)
        spinnerAssigned.onItemSelectedListener = this
        setAssignedAdapter()
    }

    fun goBack() {
        event.revert()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun deletionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.event_deletion_alert_title))
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.delete)) { _, _ -> deleteEvent() }
        builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }
        builder.show()
    }

    private fun deleteEvent() {
        event.deleteEvent()
        setResult(ViewEventActivity.RESULT_DELETED)
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
                this, R.style.TimePickerTheme, dateSetListener,
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
                this, R.style.TimePickerTheme, timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            )
            dialog.show()
        }
    }

    private fun setCheckBoxes() {
        recurrencyCheckbox.setOnCheckedChangeListener { _, b ->
            if(b) recurrencyLayout.visibility = View.VISIBLE
            else recurrencyLayout.visibility = View.GONE
        }
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

        if(event.isRecurrent()) {
            recurrencyCheckbox.isChecked = true
            recurrencyLayout.visibility = View.VISIBLE
            recurrencyNumber.setText(event.getRecurrency().toString())
        } else {
            recurrencyLayout.visibility = View.GONE
        }

        if(event.hasRecurrencyEndDate()) {
            recurrencyUntilCheckbox.isChecked = true
            untilDateDay.visibility = View.VISIBLE
            untilDateHour.visibility = View.VISIBLE
            untilDateDay.text = sdf.format(event.getRecurrencyEndDate())
            untilDateHour.text = stf.format(event.getRecurrencyEndDate())
        } else {
            untilDateDay.visibility = View.GONE
            untilDateHour.visibility = View.GONE
        }
    }

    private fun onSaveButtonClicked() {
        if(dateDay.text.isEmpty()) dateDay.error = getString(R.string.date_needed)
        else if(event.hasAssigned() && !event.checkAssignedIsCorrect()) {
            event.revert()
            setAssignedAdapter()
            val toast = Toast.makeText(
                this,
                getString(R.string.error_caregivers_updated),
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
        else {
            if (recurrencyCheckbox.isChecked) {
                if (recurrencyNumber.text.isEmpty()) recurrencyNumber.error = getString(R.string.content_mandatory)
                else {
                    if (recurrencyUntilCheckbox.isChecked) {
                        if (untilDateDay.text.isEmpty()) untilDateDay.error = getString(R.string.date_needed)
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

    private fun setAssignedAdapter() {
        caregiversList = Pet.getCaregiversFromPet(event.getPet())
        val arrayAssigned  = ArrayList(Pet.getCaregiversNamesFromPet(event.getPet()))
        arrayAssigned.add(0, getString(R.string.assigned_to_no_one))
        val adapterAssigned = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, arrayAssigned
        )
        adapterAssigned.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAssigned.adapter = adapterAssigned

        if(event.hasAssigned()) {
            val evAs = event.getAssigned()
            var done = false
            caregiversList.forEachIndexed { index, user ->
                if(user.objectId == evAs.objectId) {
                    spinnerAssigned.setSelection(index+1, true)
                    done = true
                }
                else if(index == caregiversList.lastIndex && !done) {
                    spinnerAssigned.setSelection(0)
                    val toast = Toast.makeText(
                        this,
                        getString(R.string.error_caregiver_deleted),
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
        }
        else spinnerAssigned.setSelection(0)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        when(parent) {
            spinnerEventType -> {
                typeLayout.removeAllViews()
                val transaction
                        : FragmentTransaction = supportFragmentManager.beginTransaction()
                lateinit var fragment
                        : Fragment
                when (parent.getItemAtPosition(pos).toString()) {
                    getString(R.string.vaccine) -> {
                        if(event.getDataType() == EventType.VACCINE) {
                            fragment = EditVaccineEventFragment.newInstance(event.getData() as VaccineEvent)
                            callback = fragment
                        }
                        else {
                            fragment = CreateVaccineEventFragment.newInstance()
                            callback = fragment
                        }

                    }
                    getString(R.string.food) -> {
                        if(event.getDataType() == EventType.FOOD) {
                            fragment = EditFoodEventFragment.newInstance(event.getData() as FoodEvent)
                            callback = fragment
                        }
                        else {
                            fragment = CreateFoodEventFragment.newInstance()
                            callback = fragment
                        }
                    }
                    getString(R.string.hygiene) -> {
                        if(event.getDataType() == EventType.HYGIENE) {
                            fragment = EditHygieneEventFragment.newInstance(event.getData() as HygieneEvent)
                            callback = fragment
                        }
                        else {
                            fragment = CreateHygieneEventFragment.newInstance()
                            callback = fragment
                        }
                    }
                    getString(R.string.measurement) -> {
                        if(event.getDataType() == EventType.MEASUREMENT) {
                            fragment = EditMeasurementEventFragment.newInstance(event.getData() as MeasurementEvent)
                            callback = fragment
                        }
                        else {
                            fragment = CreateMeasurementEventFragment.newInstance()
                            callback = fragment
                        }
                    }
                    getString(R.string.medicine) -> {
                        if(event.getDataType() == EventType.MEDICINE) {
                            fragment = EditMedicineEventFragment.newInstance(event.getData() as MedicineEvent)
                            callback = fragment
                        }
                        else {
                            fragment = CreateMedicineEventFragment.newInstance()
                            callback = fragment
                        }
                    }
                    getString(R.string.walk) -> {
                        if(event.getDataType() == EventType.WALK) {
                            fragment = EditWalkEventFragment.newInstance(event.getData() as WalkEvent)
                            callback = fragment
                        }
                        else {
                            fragment = CreateWalkEventFragment.newInstance()
                            callback = fragment
                        }
                    }
                }
                transaction.replace(R.id.typeLayout, fragment)
                transaction.commit()
            }
            spinnerPet -> {
                event.setPet(petList[pos])
                setAssignedAdapter()
            }
            spinnerAssigned -> {
                if(pos == 0) event.removeAssigned()
                else event.setAssigned(caregiversList[pos-1])
            }
        }
    }



    override fun onNothingSelected(parent: AdapterView<*>) {
    }

}
