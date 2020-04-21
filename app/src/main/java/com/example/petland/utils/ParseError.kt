package com.example.petland.utils

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.petland.R

class ParseError {

    fun writeParseError(a: Activity, e: com.parse.ParseException) {
        val builder = AlertDialog.Builder(a)

        if (e.message == "i/o failure") {
            builder.setTitle((R.string.errorconnect))
            e.printStackTrace()
            builder.setMessage((R.string.noconnection))
            builder.setNeutralButton("OK") { dialogInterface, which ->
                Toast.makeText(a, "", Toast.LENGTH_LONG).show()
            }
        } else {
            builder.setTitle(e.message)
            e.printStackTrace()
            builder.setMessage(e.toString())
            builder.setNeutralButton("OK") { dialogInterface, which ->
                Toast.makeText(a, "", Toast.LENGTH_LONG).show()

            }
        }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }