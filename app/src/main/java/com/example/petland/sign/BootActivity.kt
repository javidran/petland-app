package com.example.petland.sign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.pet.creation.GetFirstPetActivity
import com.example.petland.utils.ParseError
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.parse.ParseException
import com.parse.ParseUser
import java.io.File
import java.lang.Thread.sleep
import java.net.URL
import java.util.*
import kotlin.collections.HashMap


class BootActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 0
    private lateinit var signInButton: SignInButton
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    /*    private val TAG = "FragmentActivity"*/

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boot)

        signInButton = findViewById(R.id.google_sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("236588416684-p9rnb4cf36gckvo11bklu2ohg11drst0.apps.googleusercontent.com")
            .requestEmail()
            .requestProfile()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signInButton.setOnClickListener { signInGoogle() }

    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) { // The Task returned from this call is always completed, no need to attach
// a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val id = account?.id
            val idToken = account?.idToken
            val authData = HashMap<String, String?>()
            authData["id_token"] = idToken
            authData["id"] = id

            ParseUser.logInWithInBackground("google", authData).waitForCompletion()
            val user = ParseUser.getCurrentUser()
            if (user != null) {
                if (user.isNew) {
                    user.email = account?.email
                    user.username = account?.email?.replace("@gmail.com", "") //el username será el de Google
                    account?.displayName?.let { user.put("name", it) }
                    user.put("birthday", Calendar.getInstance().time)
                    try {
                        user.save()
                        startActivity(Intent(this@BootActivity, GetFirstPetActivity::class.java))
                    } catch (e: ParseException) {
                        val error = ParseError()
                        error.writeParseError(this, e)
                    }
                }
                else {
                    // Signed in successfully, show authenticated UI.
                    startActivity(Intent(this@BootActivity, HomeActivity::class.java))
                }
            }
        } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
// Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this@BootActivity, "Failed", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() { // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            startActivity(Intent(this@BootActivity, HomeActivity::class.java))
        }
        super.onStart()
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

    fun signIn(view: View) {
        val intent = Intent(this, SignInActivity::class.java).apply {
        }
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    override fun onBackPressed() {
    }

}
