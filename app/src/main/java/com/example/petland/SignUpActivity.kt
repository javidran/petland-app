package com.example.petland

import android.app.DatePickerDialog
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*


class SignUpActivity : AppCompatActivity() {

    lateinit var date: Date
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            date = cal.time
            textViewBirthday.text = sdf.format(cal.time)

        }

        textViewBirthday.setOnClickListener {
            DatePickerDialog(this@SignUpActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }


    fun signIn(view: View) {
        val intent = Intent(this, SignInActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)


    }
    fun createUser (view: View) {
        val user =   ParseUser()
        user.username = editTextUsername.text.toString()
        user.setPassword(editTextPassword.text.toString())
        user.email = editTextEmail.text.toString()

        user.put("name", editTextName.text.toString())
        user.put ("birthday", date)

        user.signUpInBackground { e ->
            if (e == null) {
                Log.d("SignUp", "Usuario creado correctamente")
            } else {
                e.printStackTrace()
                Log.d("SignUp", "No se ha podido crear el usuario")
            }
        }
    }

}