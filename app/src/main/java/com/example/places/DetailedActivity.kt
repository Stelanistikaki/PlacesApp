package com.example.places

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources


class DetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title.
        supportActionBar?.hide(); //hide the title bar.

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        val place : Place = intent.getSerializableExtra("extraPlace") as Place

        val name: TextView = findViewById(R.id.detailedName)
        val code: TextView = findViewById(R.id.detailedCode)
        val type: ImageView = findViewById(R.id.detailedIcon)
        val address: TextView = findViewById(R.id.detailedAddress)
        val tags: TextView = findViewById(R.id.detailedTags)

        name.text = place.name
        code.text = place.code
        type.setImageDrawable(place.icon)
        address.text = place.address
        val typeInt: Int = place.setTypeIcon()
        type.setImageDrawable(AppCompatResources.getDrawable(this, typeInt))

        val result = place.tags.asSequence().joinToString(separator = ", #")
        tags.text = "#$result"

        address.setOnClickListener{
            val geoLocation = Uri.parse("google.streetview:cbll=${place.location.lat},${place.location.lon}")
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = geoLocation
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

    }
}