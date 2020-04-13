package com.example.petland.user_profile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.R
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_changepassword.*


class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepassword)
    }

    fun returnHome(view: View) {
        finish()
    }

    fun changePassword(view: View) {
        val user = ParseUser.getCurrentUser()
        if (user != null) {
            if (TextUtils.isEmpty(editTextConfirmPassword.text)) {
                editTextConfirmPassword.error = getString(R.string.passwordNeeded)
            } else if (editTextPassword.text.toString() != editTextConfirmPassword.text.toString()) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    getString(R.string.passwordsDontMatch),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                user.setPassword(editTextPassword.text.toString())
                user.save()
                val intent = Intent(this, EditProfileActivity::class.java).apply {}
                startActivity(intent)
                overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                Toast.makeText(
                    this@ChangePasswordActivity,
                    getString(R.string.passwordChanged),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Log.d(TAG, getString(R.string.userNotLogged))
        }

    }

    companion object {
        private const val TAG = "Petland ChangePassword"
    }
}