package com.example.petland.user_profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.R
import com.example.petland.pet.Pet
import com.example.petland.sign.BootActivity
import com.example.petland.utils.ParseError
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_editprofile.*
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    private lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        val cal = Calendar.getInstance()
        val user = ParseUser.getCurrentUser()
        editTextUsername.setText(user.get("username").toString())
        editTextEmail.setText(user.get("email").toString())
        val formattedDate = sdf.format(user.get("birthday"))
        date = user.get("birthday") as Date
        editTextBirthday.setText(formattedDate.toString())
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

    private fun edit() {
        val user = ParseUser.getCurrentUser()
        if (user != null) {
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
                else -> {
                    user.username = editTextUsername.text.toString()
                    user.email = editTextEmail.text.toString()
                    user.put("name", editTextName.text.toString())
                    user.put("birthday", date)
                    try {
                        user.save()
                        Log.d(TAG, getString(R.string.profileEditedCorrectly))
                        Toast.makeText(
                            this@EditProfileActivity,
                            getString(R.string.profileEditedCorrectly),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } catch (e: ParseException) {
                        val error = ParseError()
                        error.writeParseError(this, e)
                        user.revert()
                    }
                }
            }
        } else {
            Log.d(TAG, getString(R.string.userNotLogged))
        }
    }

    fun changepassword(view: View) {
        val intent = Intent(this, ChangePasswordActivity::class.java).apply {}
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }
    fun deleteUserConfirmation(view: View) {
        val builder = AlertDialog.Builder(this@EditProfileActivity)
        builder.setTitle(getString(R.string.delete_account))
        builder.setMessage(getString(R.string.delete_account_message))

        builder.setPositiveButton((getString(R.string.ok))){dialog, which ->
            Toast.makeText(applicationContext,"Cuenta borrada correctamente",Toast.LENGTH_SHORT).show()
            deleteUser()
        }
        builder.setNeutralButton(getString(R.string.cancel)){_,_ ->
        }
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }


    private fun deleteUser() {
        val user = ParseUser.getCurrentUser()
        val query = ParseQuery.getQuery(Pet::class.java)
        query.whereEqualTo("owner", user) //Query para obtener todas las mascotas propiedad del user a eliminar, para eliminarlas tambien
        query.findInBackground { petsList, e ->
            if (e == null) {
                for (pet in petsList) {
                    pet.deletePet()
                }
                user.deleteInBackground { e ->
                    if (e == null) {
                        val gso =
                            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .build()
                        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                        Log.d(TAG,"User correctly deleted!") //Mensaje en logcat
                        mGoogleSignInClient.signOut()
                            .addOnCompleteListener(this) {
                                val notificationManager =
                                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.cancelAll()
                                val currentUser = ParseUser.getCurrentUser()
                                if (currentUser != null) {
                                    Log.d(TAG, getString(R.string.loggedOut)) //Mensaje en logcat
                                    ParseUser.logOut()
                                    Toast.makeText(this, getString(R.string.loggedOut), Toast.LENGTH_SHORT).show()
                                }
                                startActivity(Intent(this, BootActivity::class.java).apply {})
                                finish()
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                            }
                    } else {
                        Log.d(TAG,"An error occurred!") //Mensaje en logcat
                    }
                }
            } else {
                Log.d(TAG, "An error happened while retrieving user pets.")
            }
        }
    }

    fun savechanges(view: View) {
        confirmationDialog()
    }

    fun cancel(view: View) {
        cancelationDialog()
    }

    override fun onBackPressed() {
        cancelationDialog()
    }

    private fun cancelationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.cancelAlertTitle))
        builder.setMessage(getString(R.string.cancelAlertMessage))
        builder.setCancelable(true)
        builder.setPositiveButton(getString(R.string.ok))
        { dialog, which ->
            finish()
        }
        builder.setNegativeButton(getString(R.string.cancel))
        { dialog, which -> }
        builder.show()
    }

    private fun confirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.confirmationAlertTitle))
        builder.setMessage(getString(R.string.confirmationAlertMessage))
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.ok))
        { dialog, which ->
            edit()
        }
        builder.setNegativeButton(getString(R.string.cancel))
        { dialog, which -> }
        builder.show()
    }

    companion object {
        private const val TAG = "Petland EditProfile"
    }


}
