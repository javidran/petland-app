package com.example.petland.sign

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.pet.Pets
import com.example.petland.pet.creation.GetFirstPetActivity
import com.example.petland.user_profile.invitations.ViewInvitationsActivity
import com.example.petland.utils.ParseError
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_signin.*


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
                        //notificationEvent()
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

            val name3: CharSequence = "Nuevos eventos"
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
                    val intent = Intent(this, ViewInvitationsActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.animal_paw)
                        .setContentTitle("Nueva invitación para cuidar una mascota")
                        .setContentText("Tienes "+ invitationsList.size + " invitaciones de cuidador")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                    val notificationManagerCompat =
                        NotificationManagerCompat.from(applicationContext)
                    notificationManagerCompat.notify(NOTIFICACION_ID, builder.build())

                    with(NotificationManagerCompat.from(this)) {
                        // notificationId is a unique int for each notification that you must define
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
                    val builder = NotificationCompat.Builder(this, CHANNEL_IDX)
                        .setSmallIcon(R.drawable.animal_paw)
                        .setContentTitle("Invitación aceptada")
                        .setContentText("Tienes " + invitationsList.size + " invitaciones aceptadas de cuidadores de mascota")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    val notificationManagerCompat =
                        NotificationManagerCompat.from(applicationContext)
                    notificationManagerCompat.notify(NOTIFICACION_IDX, builder.build())

                    with(NotificationManagerCompat.from(this)) {
                        // notificationId is a unique int for each notification that you must define
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
        val builder = NotificationCompat.Builder(this, CHANNEL_IDY)
            .setSmallIcon(R.drawable.animal_paw)
            .setContentTitle("Nuevos eventos")
            .setContentText("Tienes eventos de mascota")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManagerCompat =
            NotificationManagerCompat.from(applicationContext)
        notificationManagerCompat.notify(NOTIFICACION_IDY, builder.build())

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICACION_IDY, builder.build())
        }
    }
}
