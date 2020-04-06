package com.example.petland

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.parse.ParseUser

class HomePrincipalActivity :  AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG = "Petland Dashboard"
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_principal)

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
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_mascota -> {

            }
            R.id.nav_paseos -> {

            }
            R.id.nav_mapa -> {

            }
            R.id.nav_salud -> {

            }
            R.id.nav_eventos -> {

            }
            R.id.nav_perfil -> {
                val intent = Intent(this, EditProfileActivity::class.java).apply {
                }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.nav_logout -> {
                val currentUser = ParseUser.getCurrentUser()
                if (currentUser != null) {
                    Log.d(TAG, getString(R.string.loggedOut)) //Mensaje en logcat
                    ParseUser.logOut()
                    Toast.makeText(this, getString(R.string.loggedOut), Toast.LENGTH_SHORT).show()
                    val intent = Intent(
                        this,
                        MainActivity::class.java
                    ).apply { //Para pasar de esta vista, de nuevo al SignIn
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
        override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
            R.id.action_add -> {
                // User chose the "Print" item
                Toast.makeText(this,"add action",Toast.LENGTH_LONG).show()
                true
            }

            else -> {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }
        }
    }

