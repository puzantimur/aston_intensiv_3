package com.example.astonintensiv3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ActivityLoadImageByPicasso : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_image_by_picasso)

        val editUrl = findViewById<EditText>(R.id.edit_url_of_image)
        val imageFromUrl = findViewById<ImageView>(R.id.image)

        editUrl.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loadImage(p0.toString(), imageFromUrl)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    fun loadImage(url: String, imageView: ImageView) {
        Picasso.with(this)
            .load(url)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                }

                override fun onError() {
                    Toast.makeText(
                        this@ActivityLoadImageByPicasso,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }
}
