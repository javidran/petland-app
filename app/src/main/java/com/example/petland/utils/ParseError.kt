package com.example.petland.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.example.petland.R

class ParseError {

    fun writeParseError(a: Activity, e: com.parse.ParseException) {
        val builder = AlertDialog.Builder(a)

        if (e.message == "i/o failure") {
            builder.setTitle((R.string.errorconnect))
            builder.setMessage((R.string.noconnection))
            builder.setNeutralButton("OK") { dialogInterface, which ->
            }
        } else {
            builder.setTitle(e.message)
            builder.setMessage(e.message.toString())
            builder.setNeutralButton("OK") { dialogInterface, which ->
            }
        }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }