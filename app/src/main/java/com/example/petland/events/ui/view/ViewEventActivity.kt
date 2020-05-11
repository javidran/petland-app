package com.example.petland.events.ui.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.R
import com.example.petland.events.enums.EventType
import com.example.petland.events.model.PetEvent
import com.example.petland.image.ImageUtils
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_view_event.*
import java.text.SimpleDateFormat
import java.util.*

class ViewEventActivity : AppCompatActivity() {
    lateinit var event: PetEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)

        event = intent.extras?.get("event") as PetEvent
        deleteEventButton.setOnClickListener { deletionDialog() }
        returnButton.setOnClickListener { onBackPressed() }
        checkDoneButton.setOnClickListener { chooseDoneDate() }
    }

    override fun onResume() {
        super.onResume()
        setInfo()
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
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun setInfo() {
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
            viewUntilLayout.visibility = View.VISIBLE
            val text = getString(R.string.repeat_every) + " " + event.getRecurrency() + " " + getString(R.string.days)
            viewEventRepeatable.text = text
            if(event.hasRecurrencyEndDate()) {
                viewUntilDate.text = sdf.format(event.getRecurrencyEndDate())
            }
        }
        else {
            viewUntilLayout.visibility = View.GONE
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
    }

    private fun chooseDoneDate() {
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}
