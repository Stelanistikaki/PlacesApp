package com.example.places

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG_CODE_PERMISSION_LOCATION: Int = 1
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLong: Double = 0.0
    private var currentLat: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title.
        supportActionBar?.hide(); //hide the title bar.

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var recyclerView: RecyclerView = findViewById(R.id.placesRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        val apiInterface = ApiInterface.create().getPlaces()

        apiInterface.enqueue(object : Callback<List<Place>> {
            override fun onResponse(call: Call<List<Place>>?, response: Response<List<Place>>?) {

                if (response?.body() != null) {
                    var places: List<Place> = response.body()!!
                    getCurrentLocation()
                    var recyclerAdapter = RecyclerviewItemAdapter(places, currentLat, currentLong)
                    recyclerView.adapter = recyclerAdapter
                }
            }

            override fun onFailure(call: Call<List<Place>>?, t: Throwable?) {

            }
        })
    }

    fun getCurrentLocation (){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                TAG_CODE_PERMISSION_LOCATION
            )
        }
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        val location: Location? = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            currentLong = location.longitude
            currentLat = location.latitude
        }
    }
}