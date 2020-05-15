package com.example.petland.events.ui.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petland.R
import com.example.petland.events.enums.EventType
import com.example.petland.events.model.*
import com.example.petland.events.ui.edit.EditEventActivity
import com.example.petland.image.ImageUtils
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.activity_view_event.*
import java.text.SimpleDateFormat
import java.util.*

class ViewEventActivity : AppCompatActivity() {
    private lateinit var event: PetEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)

        event = intent.extras?.get("event") as PetEvent
        returnButton.setOnClickListener { onBackPressed() }
        checkDoneButton.setOnClickListener { chooseDoneDate() }
        editEventButton.setOnClickListener { onEditEvent() }
    }

    override fun onResume() {
        super.onResume()
        setInfo()
    }

    private fun setInfo() {
        event.fetch<PetEvent>()

        val dataType = event.getDataType()
        lateinit var dataTypeString: String
        dataTypeString = when (dataType) {
            EventType.FOOD -> getString(R.string.food)
            EventType.HYGIENE -> getString(R.string.hygiene)
            EventType.MEASUREMENT -> getString(R.string.measurement)
            EventType.MEDICINE -> getString(R.string.medicine)
            EventType.VACCINE -> getString(R.string.vaccine)
            EventType.WALK -> getString(R.string.walk)
        }
        viewEventType.text = dataTypeString

        val petQuery = ParseQuery.getQuery<ParseObject>("Pet")
        petQuery.whereEqualTo("objectId", event.getPet().objectId)
        val pet : ParseObject = petQuery.find()[0]
        viewEventAssigned.text = pet.get("name").toString()
        ImageUtils.retrieveImage(pet, petEventImage)
        
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
        viewEventDate.text = sdf.format(event.getDate())

        if(event.isRecurrent()) {
            viewEventRepeatable.visibility = View.VISIBLE
            val text = getString(R.string.repeat_every) + " " + event.getRecurrency() + " " + getString(R.string.days)
            viewEventRepeatable.text = text
            if(event.hasRecurrencyEndDate()) {
                viewUntilLayout.visibility = View.VISIBLE
                viewUntilDate.text = sdf.format(event.getRecurrencyEndDate())
            } else {
                viewUntilLayout.visibility = View.GONE
            }
        }
        else {
            viewEventRepeatable.visibility = View.GONE
            viewUntilLayout.visibility = View.GONE
        }

        if(event.hasAssigned()) {
            viewAssignedLayout.visibility = View.VISIBLE
            val name = event.getAssigned().getString("name")
            if(name != null) viewAssignedName.text = name
            else viewAssignedLayout.visibility = View.GONE
        }
        else {
            viewAssignedLayout.visibility = View.GONE
        }

        if(event.isDone()) {
            checkDoneButton.visibility = View.GONE
            viewDoneLayout.visibility = View.VISIBLE
            viewDoneDate.text = sdf.format(event.getDoneDate())
        }
        else {
            checkDoneButton.visibility = View.VISIBLE
            viewDoneLayout.visibility = View.GONE
        }

        setDataLayout()
    }

    private fun setDataLayout() {
        viewEventTypeLayout.removeAllViews()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        lateinit var fragment: Fragment

        when (event.getDataType()) {
            EventType.VACCINE -> {
                fragment = ViewVaccineEventFragment.newInstance(event.getData() as VaccineEvent)
            }
            EventType.FOOD -> {
                fragment = ViewFoodEventFragment.newInstance(event.getData() as FoodEvent)
            }
            EventType.HYGIENE -> {
                fragment = ViewHygieneEventFragment.newInstance(event.getData() as HygieneEvent)
            }
            EventType.MEASUREMENT -> {
                fragment = ViewMeasurementEventFragment.newInstance(event.getData() as MeasurementEvent)
            }
            EventType.MEDICINE -> {
                fragment = ViewMedicineEventFragment.newInstance(event.getData() as MedicineEvent)
            }
            EventType.WALK -> {
                fragment = ViewWalkEventFragment.newInstance(event.getData() as WalkEvent)
            }
        }
        transaction.replace(R.id.viewEventTypeLayout, fragment)
        transaction.commit()
    }

    private fun chooseDoneDate() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.event_completion_dialog))
        builder.setCancelable(true)
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            event.markAsDone(event.getDate())
            onResume()
        }
        builder.setNegativeButton(getString(R.string.no_choose_date)) { _, _ ->
            val cal = Calendar.getInstance()

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val timeSetListener =
                        TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                            cal.set(Calendar.HOUR_OF_DAY, hour)
                            cal.set(Calendar.MINUTE, minute)
                            event.markAsDone(cal.time)
                            onResume()
                        }

                    val dialog = TimePickerDialog(
                        this, R.style.TimePickerTheme, timeSetListener,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    )
                    dialog.show()
                }

            val dialog = DatePickerDialog(
                this, R.style.TimePickerTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
        builder.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun onEditEvent() {
        val intent = Intent(this, EditEventActivity::class.java).apply {}
        intent.putExtra("event", event)
        startActivityForResult(intent, REQ_EDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ_EDIT && resultCode == RESULT_DELETED) {
            finish()
        }
    }

    companion object {
        private const val REQ_EDIT = 1000
        const val RESULT_DELETED = 96
    }

}
