package com.example.petland.sign

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.HomeActivity
import com.example.petland.utils.ParseError
import com.example.petland.R
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_signin.*


class SignInActivity : AppCompatActivity() {
    private val TAG = "Petland SignIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
    }

    fun signUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    fun progress (start:Boolean){
        if (start) {
            buttonContinuar.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
        else {
            buttonContinuar.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    fun login(view: View) {
        val intent = Intent(this, HomeActivity::class.java).apply {
        }
        when {
            TextUtils.isEmpty(editTextUsername.text) -> {
                editTextUsername.error = getString(R.string.usernameNeeded)
            }
            TextUtils.isEmpty(editTextPassword.text) -> {
                editTextPassword.error = getString(R.string.passwordNeeded)
            }
            else -> {
                progress(true)
                ParseUser.logInInBackground(
                    editTextUsername.text.toString(),
                    editTextPassword.text.toString()
                ) { user, e ->
                    if (user != null) {
                        Log.d(TAG, "User logged in correctly.")
                        startActivity(intent)
                        overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                        finish()
                    }
                    else {
                        progress(false)
                        Log.d(TAG, "User does not exist.")
                        val error = ParseError()
                        error.writeParseError(this, e)
                    }
                }
            }
        }
    }
}
