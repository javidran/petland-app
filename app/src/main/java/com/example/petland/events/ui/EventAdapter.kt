package com.example.petland.events.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.example.petland.events.enums.EventType
import com.example.petland.events.model.PetEvent
import kotlinx.android.synthetic.main.list_event_element.view.*
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(private val events: List<PetEvent>, private val context: Context, private val callback: ViewEventCallback): RecyclerView.Adapter<EventAdapter.EventHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EventHolder {
        return EventHolder(LayoutInflater.from(p0.context).inflate(R.layout.list_event_element, p0, false), callback)
    }

    override fun getItemCount(): Int {
        return events.count()
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.bindEventInfo(events[position], context)
    }


    class EventHolder(private val v: View, private val callback: ViewEventCallback) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private lateinit var event: PetEvent

        init {
            v.setOnClickListener(this)
        }

        fun bindEventInfo(event: PetEvent, context: Context) {
            this.event = event
            val dataType = event.getDataType()
            lateinit var dataTypeString: String
            dataTypeString = when (dataType) {
                EventType.FOOD -> context.getString(R.string.food)
                EventType.HYGIENE -> context.getString(R.string.hygiene)
                EventType.MEASUREMENT -> context.getString(R.string.measurement)
                EventType.MEDICINE -> context.getString(R.string.medicine)
                EventType.VACCINE -> context.getString(R.string.vaccine)
                EventType.WALK -> context.getString(R.string.walk)
            }

            v.eventTypeTextView.text = dataTypeString
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
            v.dateTextView.text = sdf.format(event.getDate())
            when {
                event.isDone() -> {
                    v.recurrencyTextView.visibility = View.VISIBLE
                    v.recurrencyTextView.text = context.getString(R.string.done)
                }
                event.isRecurrent() -> {
                    v.recurrencyTextView.visibility = View.VISIBLE
                    val text = context.getString(R.string.repeat_every) + event.getRecurrency() + context.getString(R.string.days)
                    v.recurrencyTextView.text = text
                }
                else -> {
                    v.recurrencyTextView.visibility = View.GONE
                }
            }
        }

        override fun onClick(v: View?) {
            callback.startViewEventActivity(event)
        }
    }
}