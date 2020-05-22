package com.example.petland

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petland.events.model.PetEvent
import com.example.petland.events.ui.EventsFragment
import com.example.petland.events.ui.view.ViewEventActivity
import com.example.petland.mapas.MapsFragment
import com.example.petland.mapas.TimelineFragment
import com.example.petland.pet.Pets
import com.example.petland.pet.Pets.Companion.getNamesFromPetList
import com.example.petland.pet.Pets.Companion.setSelectedPet
import com.example.petland.pet.creation.GetFirstPetActivity
import com.example.petland.sign.BootActivity
import com.example.petland.locations.ui.MapFragment
import com.example.petland.user_profile.UserProfileFragment
import com.example.petland.user_profile.invitations.ViewInvitationsActivity
import com.example.petland.utils.CustomAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.parse.ParseObject
import com.parse.ParseUser
import kotlinx.android.synthetic.main.content_home.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var spinner: Spinner
    lateinit var listPets: Array<String>
    private lateinit var objectpet: List<ParseObject>
    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if(!Pets.userHasPets()) {
            val intent = Intent(this, GetFirstPetActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if(intent.extras?.getBoolean(Application.INVITATION_NOTIFICATION) != null && intent.extras?.getBoolean(Application.INVITATION_NOTIFICATION)!!) {
                intent.putExtra(Application.INVITATION_NOTIFICATION, true)
            }
            startActivity(intent)
            finish()
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)


        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        createSpinnerPet()
        frameLayout.removeAllViews()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragment = HomePrincipalFragment.newInstance()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()

        if(intent.extras?.getBoolean(Application.INVITATION_NOTIFICATION) != null && intent.extras?.getBoolean(Application.INVITATION_NOTIFICATION)!!) {
            frameLayout.removeAllViews()
            val transactionPr: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragment = UserProfileFragment.newInstance()
            transactionPr.replace(R.id.frameLayout, fragment)
            transactionPr.commit()
            val intent = Intent(this, ViewInvitationsActivity::class.java).apply {}
            startActivity(intent)
        }

        if(intent.extras?.getBoolean(Application.EVENT_NOTIFICATION) != null && intent.extras?.getBoolean(Application.EVENT_NOTIFICATION)!!) {
            frameLayout.removeAllViews()
            val transactionEv: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragment = EventsFragment.newInstance()
            transactionEv.replace(R.id.frameLayout, fragment)
            transactionEv.commit()
            if(intent.extras?.getParcelable<PetEvent>(Application.EVENT_NOTIFICATION_INSTANCE) != null) {
                //En caso de que se reciba el evento en específico abrirá el evento
                val intent = Intent(this, ViewEventActivity::class.java).apply {}
                val ev: PetEvent = intent.extras?.getParcelable(Application.EVENT_NOTIFICATION_INSTANCE)!!
                intent.putExtra("event", ev)
                startActivity(intent)
            }
        }
    }

    private fun createSpinnerPet() {
        objectpet  = Pets.getPetsFromCurrentUser()
        listPets = getNamesFromPetList(objectpet.toList())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val item = menu!!.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner
 
        val customAdapter =
            CustomAdapter(applicationContext, objectpet, listPets)

        spinner.adapter = customAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setSelectedPet(parent?.getItemAtPosition(position) as ParseObject)
                fragment.onResume()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun clearNotifications() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_mascota -> {
                frameLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                fragment = HomePrincipalFragment.newInstance()
                transaction.replace(R.id.frameLayout, fragment)
                transaction.commit()
            }
            R.id.nav_paseos -> {
                frameLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                fragment = TimelineFragment.newInstance()
                transaction.replace(R.id.frameLayout, fragment)
                transaction.commit()
            }
            R.id.nav_mapa -> {
                frameLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                fragment = MapFragment.newInstance()
                transaction.replace(R.id.frameLayout, fragment)
                transaction.commit()
            }
            R.id.nav_salud -> {

            }
            R.id.nav_eventos -> {
                frameLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                fragment = EventsFragment.newInstance()
                transaction.replace(R.id.frameLayout, fragment)
                transaction.commit()
            }
            R.id.nav_testing -> {
                frameLayout.removeAllViews()
                val intent = Intent(this, TestingActivity::class.java).apply {
                }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.nav_perfil -> {
                frameLayout.removeAllViews()
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                fragment = UserProfileFragment.newInstance()
                transaction.replace(R.id.frameLayout, fragment)
                transaction.commit()
            }
            R.id.nav_logout -> {
                mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this) {
                        clearNotifications()
                        val currentUser = ParseUser.getCurrentUser()
                        if (currentUser != null) {
                            Log.d(TAG, getString(R.string.loggedOut)) //Mensaje en logcat
                            ParseUser.logOut()
                            Toast.makeText(this, getString(R.string.loggedOut), Toast.LENGTH_SHORT).show()
                        }
                        startActivity(Intent(this@HomeActivity, BootActivity::class.java).apply {})
                        finish()
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.spinner -> {

            Toast.makeText(this, "add action", Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        fragment.onActivityResult(requestCode, resultCode, intent)

    }

    fun iniciarPaseo() {
        frameLayout.removeAllViews()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragment = MapsFragment.newInstance()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    fun volverHome() {
        frameLayout.removeAllViews()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragment = HomePrincipalFragment.newInstance()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    fun homeAntiguo(view: View) {
        val intent = Intent(this, TestingActivity::class.java).apply {
        }

        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    companion object {

        private const val TAG = "Petland Dashboard"
    }

}

