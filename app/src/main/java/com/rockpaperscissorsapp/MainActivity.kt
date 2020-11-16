package com.rockpaperscissorsapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin


class MainActivity : AppCompatActivity() {

    private val CAMERAREQUEST = 102
    private val STORAGEREQUEST = 103

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Start Labeling Button
        val button_start_game = findViewById<Button>(R.id.btn_start_game)
        button_start_game.setOnClickListener {
            val myIntent = Intent(this, GameActivity::class.java)
            this.startActivity(myIntent)
        }

        // Perform inference Button
        val btn_labeling = findViewById<Button>(R.id.btn_labeling)
        btn_labeling.setOnClickListener {
            val myIntent = Intent(this, LabelingActivity::class.java)
            this.startActivity(myIntent)
        }


        // Log OUT button
        val btn_logout = findViewById<Button>(R.id.btn_logout)
        btn_logout.setOnClickListener {
            Amplify.Auth.signOut(
                AuthSignOutOptions.builder().globalSignOut(true).build(),
                { Log.i("AuthQuickstart", "Signed out globally")

                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish() // finish the current activity

                },
                { error -> Log.e("AuthQuickstart", error.toString()) }
            )
        }


//        Amplify.Auth.signIn(
//            "-",
//            "-",
//            { result -> Log.i("AuthQuickstart", if (result.isSignInComplete) "Sign in succeeded" else "Sign in not complete") },
//            { error -> Log.e("AuthQuickstart", error.toString()) }
//        )
//        Amplify.Auth.fetchAuthSession(
//            { result -> Log.i("AmplifyQuickstart", result.toString()) },
//            { error -> Log.e("AmplifyQuickstart", error.toString()) }
//        )
//        Amplify.Auth.signUp(
//            "-",
//            "-",
//            AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), "soeren.erichsen99@gmail.com").build(),
//            { result -> Log.i("AuthQuickStart", "Result: $result") },
//            { error -> Log.e("AuthQuickStart", "Sign up failed", error) }
//        )

//        Amplify.Auth.confirmSignUp(
//            "-",
//            "-",
//            { result -> Log.i("AuthQuickstart", if (result.isSignUpComplete) "Confirm signUp succeeded" else "Confirm sign up not complete") },
//            { error -> Log.e("AuthQuickstart", error.toString()) }
//        )
    }

    override fun onResume() {
        super.onResume()

        checkForPermission(android.Manifest.permission.CAMERA, "camera", CAMERAREQUEST)
        checkForPermission(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            "External Storage",
            STORAGEREQUEST
        )
    }


    private fun checkForPermission(permission: String, name: String, requestCode: Int) {
        /*
        This method checks if the user has granted Camera permissions

        Args:
            permission: android Manifest Camera Permission
            name: name of the permission eg. camera
            requestCode: Camera request code (int)

         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // If permission granted, show toast
            when {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {

                }
                shouldShowRequestPermissionRationale(permission) -> explainPermissionDialog(
                    permission,
                    name,
                    requestCode
                )
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        when (requestCode) {
            CAMERAREQUEST -> innerCheck("camera")
        }
    }

    private fun explainPermissionDialog(permission: String, name: String, requestCode: Int) {
        /*
            This method creates a dialog that tells the user, why the app needs a specific permission

        Args:
            permission: android Manifest Camera Permission
            name: name of the permission eg. camera
            requestCode: Camera request code (int)

        */
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access your $name is required to use this app.")
            setTitle("Permission required")
            setPositiveButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}
