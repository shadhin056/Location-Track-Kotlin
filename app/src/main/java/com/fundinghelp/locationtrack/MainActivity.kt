package com.fundinghelp.locationtrack

import android.Manifest
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fundinghelp.locationtrack.util.DeviceLocationTracker
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*

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
        btnLocation.setOnClickListener {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        checkLocation();
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    }

                    override fun onPermissionRationaleShouldBeShown( permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest();
                    }
                }).check()
        }
        }

    private fun checkLocation() {
        val toast = Toast.makeText(this, "latitude : "+currentlLat+" longitude : "+currentLng, Toast.LENGTH_LONG)
        toast.show()
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