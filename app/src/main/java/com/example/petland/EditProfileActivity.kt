package com.example.petland

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_editprofile.*
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity(){
    private val TAG = "Petland EditProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        val cal = Calendar.getInstance()
        val user = ParseUser.getCurrentUser()
        editTextUsername.setText(user.get("username").toString())
        editTextEmail.setText(user.get("email").toString())
        val dateb = sdf.format(user.get("birthday"))
        date = user.get("birthday") as Date
        editTextBirthday.setText(dateb.toString())
        editTextName.setText(user.get("name").toString())
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
                this@EditProfileActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Date().time
            dialog.show()
        }

    }
    fun edit (view: View) {
        val user= ParseUser.getCurrentUser()
        if (user != null) {
            user.username = editTextUsername.text.toString()
            user.email = editTextEmail.text.toString()
            user.put("name", editTextName.text.toString())
              user.put("birthday", date)
            user.save()
            Log.d(TAG, getString(R.string.profileEditedCorrectly))
            Toast.makeText(this@EditProfileActivity, getString(R.string.profileEditedCorrectly), Toast.LENGTH_LONG).show()
        } else {
           Log.d(TAG, getString(R.string.userNotLogged))
        }
    }
    fun volver(view: View) {
        val intent = Intent(this, HomePrincipalActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    fun changepassword(view: View) {
        val intent = Intent(this, ChangePasswordActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }



}