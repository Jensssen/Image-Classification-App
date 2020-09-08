package com.rockpaperscissorsapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class GameActivity : AppCompatActivity() {

    val CAMERA_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val btn_take_picture = findViewById<Button>(R.id.btn_take_image)

        btn_take_picture.setOnClickListener{
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null){
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val image = findViewById<ImageView>(R.id.image)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    image.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognised request code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
