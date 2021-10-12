package com.example.places

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DetailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        val place : Place = intent.getSerializableExtra("extraPlace") as Place

        val name: TextView = findViewById(R.id.detailedName)
        val code: TextView = findViewById(R.id.detailedCode)
        val type: TextView = findViewById(R.id.detailedType)
        val address: TextView = findViewById(R.id.detailedAddress)
        val tags: TextView = findViewById(R.id.detailedTags)

        name.text = place.name
        code.text = place.code
        type.text = place.type
        address.text = place.address


        address.setOnClickListener(){
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