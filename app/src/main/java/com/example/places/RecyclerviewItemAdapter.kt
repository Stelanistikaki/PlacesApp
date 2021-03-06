package com.example.places

import android.content.Context
import android.content.Intent
import android.location.Location.distanceBetween
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class RecyclerviewItemAdapter(mItemList: List<Place>, currentLang: Double, currentLong: Double) : RecyclerView.Adapter<RecyclerviewItemAdapter.ViewHolder>() {
    private var placesList : List<Place> = mItemList
    private var currentLang : Double = currentLang
    private var currentLong : Double = currentLong
    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerviewItemAdapter.ViewHolder {

        context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.list_item, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name)
        var distance: TextView = view.findViewById(R.id.distance)
        var type: ImageView = view.findViewById(R.id.typeImage)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var place: Place = this.placesList!![position]
        holder.name.text = place.name
        holder.distance.text = calculateDistance(place.location.lat, place.location.lon)
        val typeInt: Int = place.setTypeIcon()
        holder.type.setImageDrawable(AppCompatResources.getDrawable(context, typeInt))

        holder.itemView.setOnClickListener {
            //put the place object in intent extra to get it in detailed activity
            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("extraPlace", place)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    private fun calculateDistance(lat: Double, lon: Double) : String {
        //initialize a float array with size > 0
        val result = FloatArray(1)
        //get the distance with preexisting function
        distanceBetween(currentLang, currentLong, lat, lon, result)
        //the result is the first in the array
        val m = result[0]
        //make it string
        val strM = "$m"
        if (strM.length > 3) //km
        {
            var km = m * 1.0f / 1000
            km = (km * 10).roundToInt() * 1.0f / 10
            return "$km km Away"
        }
        return "$m m km Away"
    }
}