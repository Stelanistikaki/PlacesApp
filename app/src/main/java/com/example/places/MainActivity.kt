package com.example.places

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLong: Double = 0.0
    private var currentLat: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title.
        supportActionBar?.hide(); //hide the title bar.

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // Init Bespot SDK with the provided App Id and App Secret
//        Bespot.init(
//                this,
//                applicationId = "mock_sample_app",
//                applicationSecret = "password",
//                null
//        )

        //set textview for when there is no connection
        var noInternet: TextView = findViewById(R.id.noInternet)
        //by default assume there is internet connection
        noInternet.isVisible = false

        //if there is not show text and not execute the below code
        if(!isOnline()){
            noInternet.isVisible = true
            return
        }

        //var for location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //set the recycler view and the layout manager
        var recyclerView: RecyclerView = findViewById(R.id.placesRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        //call api interface
        val apiInterface = ApiInterface.create().getPlaces()

        apiInterface.enqueue(object : Callback<List<Place>> {
            override fun onResponse(call: Call<List<Place>>?, response: Response<List<Place>>?) {
                //if there are results
                if (response?.body() != null) {
                    var places: List<Place> = response.body()!!
                    //get current location to estimate how many km/m away
                    getCurrentLocation()
                    var recyclerAdapter = RecyclerviewItemAdapter(places, currentLat, currentLong)
                    recyclerView.adapter = recyclerAdapter
                }
            }

            override fun onFailure(call: Call<List<Place>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Cannot get the requested data. Maybe try again?", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getCurrentLocation (){
        //if the permission for location is granted ask for it
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ),
                    0
            )
        }
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        val location: Location? = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            //current lan and long
            currentLong = location.longitude
            currentLat = location.latitude
        }
    }

    //function to check internet connection
    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}