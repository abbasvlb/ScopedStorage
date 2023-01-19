package com.ivy.scopedstorage

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
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
            saveFileToExternalStorage("https://file-examples.com/storage/fe2879c03363c669a9ef954/2017/10/file_example_JPG_100kB.jpg","abc.png")
        })
    }

    private fun saveFileToExternalStorage(url: String, fileName: String) {
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
    }
}