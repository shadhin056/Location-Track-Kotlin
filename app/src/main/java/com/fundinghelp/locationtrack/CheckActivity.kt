package com.fundinghelp.locationtrack

import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.fundinghelp.locationtrack.util.DeviceLocationTracker
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import kotlinx.android.synthetic.main.activity_check.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckActivity : AppCompatActivity(), DeviceLocationTracker.DeviceLocationListener {
    private lateinit var deviceLocationTracker: DeviceLocationTracker
    private var currentlLat: Double = 0.0
    private var currentLng: Double = 0.0
    private var Country: String = ""
    private var cityName: String = ""
    private lateinit var alertDialog : LottieAlertDialog
    private  var count : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        btnLocationAgain.setOnClickListener {
            checkLocation();
        }
    }

    override suspend fun onDeviceLocationChanged(results: List<Address>?) {
        val currntLocation = results?.get(0);
        currntLocation?.apply {
            currentlLat = latitude
            currentLng = longitude
            Country = countryCode
            cityName = getAddressLine(0)
        }
        Log.e("XXX", "latitude : " + currentlLat + " longitude : " + currentLng)

        if(currentlLat!=0.0 && count ==1){
            alertDialog.dismiss()
            withContext(Dispatchers.Main) {
                count=0
                checkLocationFirstTime()
            }
        }
    }
    private fun checkLocationFirstTime() {
        val toast = Toast.makeText(
            this,
            "latitude : " + currentlLat + " longitude : " + currentLng,
            Toast.LENGTH_SHORT
        )
        toast.show()
    }
    private fun checkLocation() {

        deviceLocationTracker = DeviceLocationTracker(this, this)
        if(currentlLat==0.0){
            count++;
            alertDialog= LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                .setTitle("Loading")
                .setDescription("Please Wait")
                .build()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }else{

            val toast = Toast.makeText(
                this,
                "latitude : " + currentlLat + " longitude : " + currentLng,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }


    }
}