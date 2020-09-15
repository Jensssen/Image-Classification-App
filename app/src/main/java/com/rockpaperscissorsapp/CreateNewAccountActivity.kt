package com.rockpaperscissorsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify

class CreateNewAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_account)

        // Create Account Button
        val et_username = findViewById<EditText>(R.id.et_username)
        val et_password = findViewById<EditText>(R.id.et_password)
        val et_email = findViewById<EditText>(R.id.et_email)
        val btn_create_account = findViewById<Button>(R.id.btn_createAccount)
        val et_secret_number = findViewById<EditText>(R.id.et_secNumber)
        val tv_secret_number = findViewById<TextView>(R.id.tv_scuritynumber)
        btn_create_account.setOnClickListener {
            Amplify.Auth.signUp(
                et_username.text.toString(),
                et_password.text.toString(),
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), et_email.text.toString()).build(),
                { result -> Log.i("AuthQuickStart", "Result: $result")
                if(result.isSignUpComplete){
                    Log.i("AuthQuickStart", "SUCCESS")

                }
                },
                { error -> Log.e("AuthQuickStart", "Sign up failed", error) }
            )

        }

        Amplify.Auth.confirmSignUp(
            "jensen",
            "842617",
            { result -> Log.i("AuthQuickstart", if (result.isSignUpComplete) "Confirm signUp succeeded" else "Confirm sign up not complete") },
            { error -> Log.e("AuthQuickstart", error.toString()) }
        )
    }



}