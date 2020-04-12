package com.example.petland.utils

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class ParseError {

    fun writeParseError(a: Activity, e: com.parse.ParseException) {
        val builder = AlertDialog.Builder(a)
        builder.setTitle(e.message)
        e.printStackTrace()
        builder.setMessage(e.toString())
        builder.setNeutralButton("OK") { dialogInterface, which ->
            Toast.makeText(a, "", Toast.LENGTH_LONG).show()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}