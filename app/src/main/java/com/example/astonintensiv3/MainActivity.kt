package com.example.astonintensiv3

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.astonintensiv3.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var image: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }


        with(binding) {
            if (savedInstanceState != null) {
                val file = File(filesDir, FILE_NAME)
                imageViewFromUrl.setImageDrawable(Drawable.createFromPath(file.toString()))
            }
            buttonFlags.setOnClickListener {
                val intent = Intent(this@MainActivity, ActivityFlags::class.java)
                startActivity(intent)
            }
            buttonLoadByPicasso.setOnClickListener {
                val intent = Intent(
                    this@MainActivity,
                    ActivityLoadImageByPicasso::class.java
                )
                startActivity(intent)
            }
            buttonFindImage.setOnClickListener {
                val executor = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())
                executor.execute {
                    val imageURL = editUrl.text.toString()
                    try {
                        val stream = java.net.URL(imageURL).openStream()
                        image = BitmapFactory.decodeStream(stream)
                        handler.post {
                            imageViewFromUrl.setImageBitmap(image)
                        }
                    } catch (e: Exception) {
                        handler.post {
                            Toast.makeText(
                                this@MainActivity,
                                "oops, something went wrong",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        image?.let { saveFileToInternalStorage(it) }
        super.onSaveInstanceState(outState)

    }

    private fun saveFileToInternalStorage(bitmap: Bitmap): Boolean {
        return try {
            this.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { stream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Couldn't save bitmap")
                }
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    companion object {
        private const val FILE_NAME = "NAME_FILE"
    }
}
