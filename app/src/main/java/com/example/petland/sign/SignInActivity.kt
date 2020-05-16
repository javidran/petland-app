package com.example.petland.sign

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.petland.Application
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.events.model.PetEvent
import com.example.petland.pet.Pets
import com.example.petland.pet.creation.GetFirstPetActivity
import com.example.petland.utils.ParseError
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_signin.*
import java.text.SimpleDateFormat
import java.util.*


class SignInActivity : AppCompatActivity() {
    private val CHANNEL_ID = "Nuevas Invitaciones"
    private val NOTIFICACION_ID = 0
    private val CHANNEL_IDX = "Invitaciones aceptadas"
    private val NOTIFICACION_IDX = 1
    private val CHANNEL_IDY = "Eventos"
    private val NOTIFICACION_IDY = 2

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

    fun progress(start: Boolean) {
        if (start) {
            buttonContinuar.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            buttonContinuar.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    fun login(view: View) {

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
                    Hasher.hash(editTextPassword.text.toString())
                ) { user, e ->
                    if (user != null) {
                        Log.d(TAG, "User logged in correctly.")
                        if(Pets.userHasPets()){
                            val intent = Intent(this, HomeActivity::class.java).apply {}
                            startActivity(intent)
                            finish()
                        }
                        else{
                            val intentNoPets = Intent(this, GetFirstPetActivity::class.java).apply {
                            }
                            startActivity(intentNoPets)
                            finish()
                        }
                        overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                        createNotificationChannels()
                        notificationNewInvitation()
                        notificationAcceptCaregiver()
                        notificationEvent()
                    } else {
                        progress(false)
                        Log.d(TAG, "User does not exist.")
                        val error = ParseError()
                        error.writeParseError(this, e)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "Petland SignIn"
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Nuevas invitaciones a cuidador"
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

            val name2: CharSequence = "Invitaciones a cuidador aceptadas"
            val notificationChannelX =
                NotificationChannel(CHANNEL_IDX, name2, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManagerX =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManagerX.createNotificationChannel(notificationChannelX)

            val name3: CharSequence = "Eventos"
            val notificationChannelY =
                NotificationChannel(CHANNEL_IDY, name3, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManagerY =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManagerY.createNotificationChannel(notificationChannelY)
        }
    }

    private fun notificationNewInvitation() {
        val cUser = ParseUser.getCurrentUser()
        val query = ParseQuery.getQuery<ParseObject>("Invitation")
        query.whereEqualTo("receiver", cUser )
        query.whereEqualTo("answer", false )
        query.findInBackground { invitationsList, e ->
            if (e == null) {
                if (invitationsList.size > 0) {
                    val events = arrayOfNulls<String>(invitationsList.size)
                    for ((num, i) in invitationsList.withIndex()) {
                        val creator = i.get("creator") as ParseObject
                        creator.fetch<ParseObject>()
                        val pet = i.get("petO") as ParseObject
                        pet.fetch<ParseObject>()
                        val creatorN: String? = creator.getString("name")
                        val petN: String? = pet.getString("name")
                        events[num] = ("$creatorN te ha invitado a ser cuidador de $petN")
                    }

                    val intent = Intent(this, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    intent.putExtra(Application.INVITATION_NOTIFICATION, true)
                    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 10, intent, 0)

                    val inboxStyle =
                        NotificationCompat.InboxStyle()
                    inboxStyle.setBigContentTitle("Nuevas invitaciones para cuidar una mascota")

                    for (element in events) {
                        inboxStyle.addLine(element)
                    }

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.animal_paw)
                        .setContentTitle("Nuevas invitaciones para cuidar una mascota")
                        .setContentText("Tienes "+ invitationsList.size + " invitaciones de cuidador")
                        .setStyle( inboxStyle )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setColor(Color.MAGENTA)
                        .setAutoCancel(true)
                    
                    with(NotificationManagerCompat.from(this)) {
                        notify(NOTIFICACION_ID, builder.build())
                    }
                }
            }
        }
    }
    private fun notificationAcceptCaregiver() {
        val cUser = ParseUser.getCurrentUser()
        val query = ParseQuery.getQuery<ParseObject>("Invitation")
        query.whereEqualTo("creator", cUser )
        query.whereEqualTo("answer", true )
        query.findInBackground { invitationsList, e ->
            if (e == null) {
                if (invitationsList.size > 0) {
                    val events = arrayOfNulls<String>(invitationsList.size)
                    for ((num, i) in invitationsList.withIndex()) {
                        val creator = i.get("receiver") as ParseObject
                        creator.fetch<ParseObject>()
                        val pet = i.get("petO") as ParseObject
                        pet.fetch<ParseObject>()
                        events[num] = ("" + creator.getString("name") + " ha aceptado tu invitació para ser cuidador de " + pet.getString("name"))
                    }

                    val inboxStyle =
                        NotificationCompat.InboxStyle()
                    inboxStyle.setBigContentTitle("Invitaciones aceptadas")
                    for (element in events) {
                        inboxStyle.addLine(element)
                    }

                    val builder = NotificationCompat.Builder(this, CHANNEL_IDX)
                        .setSmallIcon(R.drawable.animal_paw)
                        .setContentTitle("Invitaciones aceptadas")
                        .setContentText("Tienes " + invitationsList.size + " invitaciones aceptadas de cuidadores de mascota")
                        .setStyle( inboxStyle )
                        .setColor(Color.CYAN)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)

                    with(NotificationManagerCompat.from(this)) {
                        notify(NOTIFICACION_IDX, builder.build())
                    }

                    for ( i in invitationsList) {
                        i.deleteInBackground()
                    }
                }
            }
        }
    }


    private fun notificationEvent() {
        val petsList =  PetEvent.getEventsFromUser()
        val today = Calendar.getInstance().time
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH , 1)
        val tomorrow = date.time

        val events = arrayOfNulls<String>(petsList.size)
        var mostrar = false
        for((num, i) in petsList.withIndex()) {
            val eventDate = i.getDate()
            if ( today < eventDate &&  eventDate < tomorrow) {
                if (!mostrar) {mostrar = true}
                val pet = i.getPet()
                val sdf = SimpleDateFormat("HH:mm aa", Locale.US)
                events[num] =
                    ("" + pet.getString("name") + " tiene un evento a las " + sdf.format(eventDate) + "")
            }
        }

        if (mostrar) {
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            intent.putExtra(Application.EVENT_NOTIFICATION, true)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 20, intent, 0)

            val inboxStyle = NotificationCompat.InboxStyle()
            inboxStyle.setBigContentTitle("Eventos")
            for (element in events) {
                inboxStyle.addLine(element)
            }

            val builder = NotificationCompat.Builder(this, CHANNEL_IDY)
                .setSmallIcon(R.drawable.animal_paw)
                .setContentTitle("Evento")
                .setContentText("Tienes eventos de mascotas para hoy")
                .setStyle(inboxStyle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                notify(NOTIFICACION_IDY, builder.build())
            }
        }
    }
}


