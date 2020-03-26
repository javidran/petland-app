package com.example.petland

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_changepassword.editTextConfirmPassword
import kotlinx.android.synthetic.main.activity_changepassword.editTextPassword


class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepassword)
    }

    fun changePassword(view: View) {
        val user = ParseUser.getCurrentUser()
        if (user != null) {
            if (TextUtils.isEmpty(editTextConfirmPassword.getText())) {
                editTextConfirmPassword.setError("Contraseña necesaria")
            } else if (editTextPassword.getText().toString() != editTextConfirmPassword.getText()
                    .toString()
            ) {
                Toast.makeText(
                        this@ChangePasswordActivity,
                        "Las contraseñas no coinciden",
                        Toast.LENGTH_LONG
                    )
                    .show()
            } else {
                user.setPassword(editTextPassword.text.toString())
                user.save()
                val intent = Intent(this, EditProfileActivity::class.java).apply {
                }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Contraseña cambiada correctamente",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Log.d("log", "No se ha hecho log in en la aplicacion")
        }

    }
}