package com.example.petland.events.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.petland.R
import com.example.petland.events.model.PetEvent
import com.parse.ParseObject
import kotlinx.android.synthetic.main.activity_create_event.*
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

class CreateEventActivity : AppCompatActivity() {
    lateinit var myPet: ParseObject
    lateinit var event: PetEvent
    var data : ParseObject? = null

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







}
