package com.example.petland.events.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.R
import com.example.petland.events.model.PetEvent
import kotlinx.android.synthetic.main.activity_view_event.*

class ViewEventActivity : AppCompatActivity() {
    lateinit var event: PetEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_event)

        event = intent.extras?.get("event") as PetEvent
        deleteEventButton.setOnClickListener { deletionDialog() }
    }

    private fun deletionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.event_deletion_alert_title))
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.delete)) { _, _ -> deleteEvent() }
        builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }
        builder.show()
    }

    private fun deleteEvent() {
        event.deleteEvent()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

}
