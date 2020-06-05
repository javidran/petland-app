package com.example.petland.health

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.events.enums.FilterEvent
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.EventAdapter
import com.example.petland.events.ui.callback.ViewEventCallback
import com.example.petland.events.ui.view.ViewEventActivity
import kotlinx.android.synthetic.main.activity_medical_history.*

class MedicalHistoryActivity : AppCompatActivity(), ViewEventCallback {

    private lateinit var recyclerView: RecyclerView

    private lateinit var layoutManagerVac: LinearLayoutManager
    private lateinit var layoutManagerMed: LinearLayoutManager
    private lateinit var adapter: EventAdapter
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_history)

        this.buttonVolver.setOnClickListener { goBack() }

        layoutManagerVac = LinearLayoutManager(this)
        layoutManagerMed = LinearLayoutManager(this)

        Historial()
    }

    override fun onResume() {
        super.onResume()
        Historial()
    }

    fun Historial() {
        recyclerViewEventsVac.isNestedScrollingEnabled = false
        recyclerViewEventsVac.layoutManager = layoutManagerVac
        recyclerViewEventsMed.isNestedScrollingEnabled = false
        recyclerViewEventsMed.layoutManager = layoutManagerMed

        this.adapter = EventAdapter(PetEvent.getEventsFromPetDone(FilterEvent.ONLY_VACCINE), this, this)
        recyclerViewEventsVac.adapter = adapter

        this.adapter = EventAdapter(PetEvent.getEventsFromPetDone(FilterEvent.ONLY_MEDICINE), this, this)
        recyclerViewEventsMed.adapter = adapter
    }

    override fun startViewEventActivity(event: PetEvent) {
        val intent = Intent(this, ViewEventActivity::class.java).apply {}
        intent.putExtra("event", event)
        startActivity(intent)
    }

    fun goBack() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
