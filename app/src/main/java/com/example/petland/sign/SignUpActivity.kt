package com.example.petland.sign

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.utils.ParseError
import com.example.petland.R
import com.example.petland.pet.creation.GetFirstPetActivity
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_signup.*
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {
    private val TAG = "Petland SignUp"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date = cal.time
                textViewBirthday.text = sdf.format(cal.time)
            }

        textViewBirthday.setOnClickListener {
           val dialog = DatePickerDialog(
                this@SignUpActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Date().time
            dialog.show()
        }
    }

    fun signIn(view: View) {
        val intent = Intent(this, SignInActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

    private fun progress (start:Boolean){
        if (start) {
            buttonCrearCuenta.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
        else {
            buttonCrearCuenta.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    fun createUser(view: View) {
        //Comprobacion de campos correcta
        when {
            TextUtils.isEmpty(editTextUsername.text) -> {
                editTextUsername.error = getString(R.string.usernameNeeded)
            }
            TextUtils.isEmpty(editTextEmail.text) -> {
                editTextEmail.error = getString(R.string.emailNeeded)
            }
            TextUtils.isEmpty(editTextName.text) -> {
                editTextName.error = getString(R.string.nameNeeded)
            }
            TextUtils.isEmpty(editTextBirthday.text) -> {
                editTextBirthday.error = getString(R.string.birthdayNeeded)
            }
            TextUtils.isEmpty(editTextPassword.text) -> {
                editTextPassword.error = getString(R.string.passwordNeeded)
            }
            TextUtils.isEmpty(editTextConfirmPassword.text) -> {
                editTextConfirmPassword.error = getString(R.string.passwordNeeded)
            }
            editTextPassword.text.toString() != editTextConfirmPassword.text.toString() -> {
                Toast.makeText(this@SignUpActivity, getString(R.string.passwordsDontMatch), Toast.LENGTH_LONG).show()
            }
            else -> {
                progress(true)
                //User creation
                val user = ParseUser()
                user.username = editTextUsername.text.toString()
                user.setPassword(editTextPassword.text.toString())
                user.email = editTextEmail.text.toString()
                user.put("name", editTextName.text.toString())
                user.put("birthday", date)

                val intent = Intent(this, GetFirstPetActivity::class.java).apply {}

                user.signUpInBackground { e ->
                    if (e == null) {
                        Log.d(TAG, "User created correctly")
                        startActivity(intent)
                    } else {
                        e.printStackTrace()
                        val error = ParseError()
                        error.writeParseError(this, e)
                        Log.d(TAG, "User could not be created")
                    }
                }
                progress(false)
            }
        }
    }
}
