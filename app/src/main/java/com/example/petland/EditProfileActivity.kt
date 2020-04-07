package com.example.petland

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.content_edit_profile.*
import kotlinx.android.synthetic.main.content_home_principal.*
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val TAG = "Petland EditProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    lateinit var date: Date
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
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

        val textViewBirthday: TextView = findViewById(R.id.editTextBirthday)
        val cal = Calendar.getInstance()
        val user = ParseUser.getCurrentUser()
        editTextUsername.setText(user.get("username").toString())
        editTextEmail.setText(user.get("email").toString())
        val dateb = sdf.format(user.get("birthday"))
        date = user.get("birthday") as Date
        editTextBirthday.setText(dateb.toString())
        editTextName.setText(user.get("name").toString())
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date = cal.time
                textViewBirthday.text = sdf.format(cal.time)
            }

        textViewBirthday.setOnClickListener {
            val dialog = DatePickerDialog(
                this@EditProfileActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.datePicker.maxDate = Date().time
            dialog.show()
        }

    }
    fun edit (view: View) {
        val user= ParseUser.getCurrentUser()
        if (user != null) {
            user.username = editTextUsername.text.toString()
            user.email = editTextEmail.text.toString()
            user.put("name", editTextName.text.toString())
              user.put("birthday", date)
            user.save()
            Log.d(TAG, getString(R.string.profileEditedCorrectly))
            Toast.makeText(this@EditProfileActivity, getString(R.string.profileEditedCorrectly), Toast.LENGTH_LONG).show()
        } else {
           Log.d(TAG, getString(R.string.userNotLogged))
        }
    }
    fun volver(view: View) {
        val intent = Intent(this, HomePrincipalActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    fun changepassword(view: View) {
        val intent = Intent(this, ChangePasswordActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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

