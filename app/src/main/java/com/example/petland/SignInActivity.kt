package com.example.petland

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.parse.Parse
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_signin.*


class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
    }

    fun signUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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
        val intent = Intent(this, MenuActivity::class.java).apply {
        }
       if (TextUtils.isEmpty(editTextUsername.getText())) {
            editTextUsername.setError("Username necesario")
        }
        else if (TextUtils.isEmpty(editTextPassword.getText())) {
            editTextPassword.setError("Contraseña necesaria")
        }
        else {
           progress(true)
            ParseUser.logInInBackground(
                editTextUsername.text.toString(),
                editTextPassword.text.toString()
            ) { user, e ->
                if (user != null) {
                    Log.d("Login", "El usuario ha entrado correctamente")
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                else {
                    progress(false)
                    Log.d("Login", "No existe usuario")
                    var error = ParseError()
                    error.escribir(this, e)
                }
            }
        }
    }
}
