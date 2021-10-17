package com.example.places

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
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

        //get the place object that was clicked on
        val place : Place = intent.getSerializableExtra("extraPlace") as Place

        //create the views
        val name: TextView = findViewById(R.id.detailedName)
        val code: TextView = findViewById(R.id.detailedCode)
        val type: ImageView = findViewById(R.id.detailedIcon)
        val address: TextView = findViewById(R.id.detailedAddress)
        val tags: TextView = findViewById(R.id.detailedTags)
        val checkIn: Button = findViewById(R.id.checkinButton)

        //set the views from the chosen place
        name.text = place.name
        code.text = place.code
        type.setImageDrawable(place.icon)
        address.text = place.address
        //use the function from place to get the icon for this specific type
        val typeInt: Int = place.setTypeIcon()
        type.setImageDrawable(AppCompatResources.getDrawable(this, typeInt))

        //get tags from array
        val result = place.tags.asSequence().joinToString(separator = ", #")
        //if the tags are not empty show them with #
        if(result.isNotEmpty())
            tags.text = "#$result"
        //if they are empty show an info text
        else
            tags.text = "No tags are available for this place.."

        //when address is clicked the use will navigate to this address in maps
        address.setOnClickListener{
            val geoLocation = Uri.parse("google.streetview:cbll=${place.location.lat},${place.location.lon}")
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = geoLocation
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        //would handle the check in action from Bespot SDK
        checkIn.setOnClickListener {}
    }
}