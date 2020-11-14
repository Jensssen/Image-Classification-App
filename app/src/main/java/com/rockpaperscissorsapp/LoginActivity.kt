package com.rockpaperscissorsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin

class LoginActivity : AppCompatActivity() {

    private var userName: String = "1" //soeren
    private var password: String = "2" //rockpaper123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }


        val user = User("", "")


        // Check if user is already signed in
        Amplify.Auth.fetchAuthSession(
            { result ->
                Log.i("AmplifyQuickstart", result.toString())
                if (result.isSignedIn) {
                    Log.i("AmplifyQuickstart", "YEY, you are signed in")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.i("AmplifyQuickstart", "NO, you are not signed in")
                }
            },
            { error -> Log.e("AmplifyQuickstart", error.toString()) }
        )


        // USERNAME EDIT TEXT
        val et_username = findViewById<EditText>(R.id.et_username)

        // Password EDIT TEXT
        val et_password = findViewById<EditText>(R.id.et_password)

        // Sign In Button
        val btn_signIn = findViewById<Button>(R.id.btn_signIn)
        btn_signIn.setOnClickListener {
            user.username = et_username.text.toString()
            user.password = et_password.text.toString()
            signIn(user)
        }
        // Create New Account Button
        val btn_newAccount = findViewById<Button>(R.id.btn_createnewaccount)
        btn_newAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, CreateNewAccountActivity::class.java)
            startActivity(intent)
        }


    }

    private fun signIn(user: User) {
        Amplify.Auth.signIn(
            user.username,
            user.password,
            { result ->
                if (result.isSignInComplete) {
                    Log.i("AmplifyQuickstart", "Sign in succeeded")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.i("AmplifyQuickstart", "Sign in not complete")
                }
            },
            { error ->
                Log.e("AmplifyQuickstart", error.toString())
            }
        )
    }
}