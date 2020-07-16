package com.fundinghelp.locationtrack

import android.location.Address
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fundinghelp.locationtrack.util.DeviceLocationTracker

class MainActivity : AppCompatActivity(), DeviceLocationTracker.DeviceLocationListener{

    private lateinit var deviceLocationTracker: DeviceLocationTracker
    private var currentlLat:Double = 0.0
    private var currentLng:Double = 0.0
    private var Country:String = ""
    private var cityName:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        deviceLocationTracker= DeviceLocationTracker(this, this)
        val toast = Toast.makeText(this, "latitude : "+currentlLat+" longitude : "+currentLng, Toast.LENGTH_LONG)
    }

    override fun onDeviceLocationChanged(results: List<Address>?) {
        val currntLocation = results?.get(0);
        currntLocation?.apply {
            currentlLat = latitude
            currentLng = longitude
            Country = countryCode
            cityName = getAddressLine(0)
        }
        Log.e("XXX","latitude : "+currentlLat+" longitude : "+currentLng)
    }
}