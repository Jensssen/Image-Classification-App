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
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify


class MainActivity : AppCompatActivity() {

    private val CAMERAREQUEST = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button = findViewById<Button>(R.id.btn_start_game)
        button.setOnClickListener {
            val myIntent = Intent(this, GameActivity::class.java)
            this.startActivity(myIntent)
        }

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("MyAmplifyApp", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error)
        }
        Amplify.Auth.fetchAuthSession(
            { result -> Log.i("AmplifyQuickstart", result.toString()) },
            { error -> Log.e("AmplifyQuickstart", error.toString()) }
        )

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        checkForPermission(android.Manifest.permission.CAMERA, "camera", CAMERAREQUEST)


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
        fun innerCheck(name: String){
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
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
