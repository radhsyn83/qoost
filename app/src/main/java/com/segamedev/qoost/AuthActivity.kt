package com.segamedev.qoost

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_auth.*
import org.jetbrains.anko.toast


class AuthActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        bt_google_login.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this) {
                it.errorMessage?.let { it1 -> toast(it1) }
            }
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("FIREBASE_LOG", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("FIREBASE_LOG", "firebaseAuthWithGoogle:" + acct.displayName)
    }

}
