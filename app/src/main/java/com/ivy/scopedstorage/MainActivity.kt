package com.ivy.scopedstorage

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ivy.scopedstorage.R.id
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var downloadButton:Button = findViewById(id.download)
        downloadButton.setOnClickListener(View.OnClickListener {
            Log.d("Log","My Message")
            saveFileToExternalStorage("https://file-examples.com/storage/fe2879c03363c669a9ef954/2017/10/file_example_JPG_100kB.jpg","abc_regular.jpeg")
        })

        var downloadButton2:Button = findViewById(id.button2)
        downloadButton2.setOnClickListener(View.OnClickListener {
            Log.d("Log","My Message")
            saveFileUsingMediaStore(this,"https://file-examples.com/storage/fe2879c03363c669a9ef954/2017/10/file_example_JPG_100kB.jpg","abc_mediastore.jpeg")
        })
    }

    private fun saveFileToExternalStorage(url: String, fileName: String) {
        try {
            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            Thread {
                URL(url).openStream().use { input ->
                    FileOutputStream(target).use { output ->
                        input.copyTo(output)
                    }
                }
                runOnUiThread {
                    // Post the result to the main thread
                }
            }.start()
        }catch (e:Exception){
            Toast.makeText(this,"Exception",Toast.LENGTH_LONG).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveFileUsingMediaStore(context: Context, url: String, fileName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            Thread {
                URL(url).openStream().use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                    }
                }
            }.start()
        }
    }

}