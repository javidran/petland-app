package com.example.petland

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_editprofile.*
import java.util.*

class EditProfileActivity : AppCompatActivity(){

    lateinit var date: Date
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd.MM.yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                date = cal.time
                textViewBirthday.text = sdf.format(cal.time)
            }

        textViewBirthday.setOnClickListener {
            DatePickerDialog(
                this@EditProfileActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }
    fun edit (view: View) {
        val user= ParseUser.getCurrentUser();
        if (user != null) {
            user.username = editTextUsername.text.toString()
            user.setPassword(editTextPassword.text.toString())
            user.email = editTextEmail.text.toString()
            user.put("name", editTextName.text.toString())
            user.put("birthday", date)
        } else {
           Log.d("log", "No se ha hecho log in en la aplicacion")
        }
    }



}