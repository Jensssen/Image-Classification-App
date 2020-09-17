package com.rockpaperscissorsapp

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.amplifyframework.core.Amplify
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class GameActivity : AppCompatActivity() {

    val CAMERA_REQUEST_CODE = 0
    var classlabel:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val btn_rock = findViewById<Button>(R.id.btn_rock)
        val btn_paper = findViewById<Button>(R.id.btn_paper)
        val btn_scissor = findViewById<Button>(R.id.btn_scissor)

        btn_rock.setOnClickListener{
            classlabel = "rock"
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null){
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }
        btn_paper.setOnClickListener{
            classlabel = "paper"
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null){
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }

        btn_scissor.setOnClickListener{
            classlabel = "scissor"
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null){
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }


//        btn_upload.setOnClickListener{
//            uploadFile()
//        }
        Amplify.Auth.fetchAuthSession(
            { result -> Log.i("AmplifyQuickstart", result.toString()) },
            { error -> Log.e("AmplifyQuickstart", error.toString()) }
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val image = findViewById<ImageView>(R.id.image)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    //val url = saveImage(data.extras?.get("data") as Bitmap)
                    var file = saveImage(data.extras?.get("data") as Bitmap, applicationContext,"rockPaperScissors")
                    uploadFile(file)
                    //tv_image_url.setText(url.toString())
                    Log.d("aaa", "bbb")
                    image.setImageBitmap(data.extras?.get("data") as Bitmap)
                    Log.d("aaa", "bbb")
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognised request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to save an image to gallery and return uri
    private fun saveImage(image:Bitmap):Uri{


        // Save image to gallery
        val savedImageURL = MediaStore.Images.Media.insertImage(
            contentResolver,
            image,
            "lololololol",
            "Image of $title"
        )

        // Parse the gallery image url to uri
        return Uri.parse(savedImageURL)
    }


    private fun uploadFile(file: File) {

        Amplify.Storage.uploadFile(
            file.name,
            file,
            { result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()) },
            { error -> Log.e("MyAmplifyApp", "Upload failed", error) }
        )
    }


    /// @param folderName can be your app's name
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String): File {

        val directory = File(Environment.getExternalStorageDirectory().toString() + separator + folderName)
        // getExternalStorageDirectory is deprecated in API 29

        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileName = System.currentTimeMillis().toString() + ".png"
        val file = File(directory, fileName)
        saveImageToStream(bitmap, FileOutputStream(file))
        if (file.absolutePath != null) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
        return file

    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
