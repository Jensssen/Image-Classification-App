package com.rockpaperscissorsapp

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.amplifyframework.core.Amplify
import kotlinx.android.synthetic.main.activity_game.*
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class GameActivity : AppCompatActivity() {

    val CAMERA_REQUEST_CODE = 0
    var rock_probability = 0.0
    var paper_probability = 0.0
    var scissor_probability = 0.0
    var class_label = 0
    var labels = arrayOf("Rock", "Paper", "Scissor")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val btn_inference = findViewById<Button>(R.id.btn_inference)

        btn_inference.setOnClickListener{
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null){
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }

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
                    var file = saveImage(data.extras?.get("data") as Bitmap, applicationContext,"inference")
                    uploadFile(file)
                    image.setImageBitmap(data.extras?.get("data") as Bitmap)
                }
            }
            else -> {
                Toast.makeText(this, "Unrecognised request code", Toast.LENGTH_SHORT).show()
            }
        }

        Thread.sleep(9000)
        downloadFile("inference_result/inference_result.txt")
    }

    private fun uploadFile(file: File) {
        Amplify.Storage.uploadFile(
            "inference/inference.png",
            file,
            { result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()) },
            { error -> Log.e("MyAmplifyApp", "Upload failed", error) }
        )
    }

    private fun downloadFile(key: String) {
        Log.i("MyAmplifyApp", applicationContext.filesDir.toString())
        Amplify.Storage.downloadFile(
            key,
            File("/storage/emulated/0/Download/sagemaker_inference_result.txt"),
            { result ->
                Log.i(
                    "MyAmplifyApp",
                    "Successfully downloaded: ${result.getFile().name}"
                )
                val inputStream: InputStream = result.file.inputStream()

                val inputString = inputStream.bufferedReader().use { it.readText() }
                Log.i("MyAmplifyApp", inputString)
                val strs = inputString.split(",").toTypedArray()


                rock_probability = strs[0].replace("\"", "").toDouble() * 100
                paper_probability = strs[1].replace("\"", "").toDouble() * 100
                scissor_probability = strs[2].replace("\"", "").toDouble() * 100
                class_label = strs[3].replace("\"", "").toInt()

                tv_rock_probability.setText("Rock Probability: " + rock_probability.toString())
                tv_paper_probability.setText("Paper Probability: " + paper_probability.toString())
                tv_scissor_probability.setText("Scisso Probability: " + scissor_probability.toString())
                tv_prediction.setText("Predicted Label: " + labels.get(class_label))
            },
            { error -> Log.e("MyAmplifyApp", "Download Failure", error) }
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
