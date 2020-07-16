package com.fundinghelp.locationtrack

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), DeviceLocationTracker.DeviceLocationListener {

    private lateinit var deviceLocationTracker: DeviceLocationTracker
    private var currentlLat: Double = 0.0
    private var currentLng: Double = 0.0
    private var Country: String = ""
    private var cityName: String = ""
    private lateinit var alertDialog : LottieAlertDialog
    private  var count : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //deviceLocationTracker= DeviceLocationTracker(this, this)
        btnLocation.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (doesUserHavePermission()) {
                    checkLocation();
                } else {
                    Dexter.withContext(this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                checkLocation();
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                if (response.isPermanentlyDenied()) {
                                    permissionNotFount();
                                }
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                permission: PermissionRequest,
                                token: PermissionToken
                            ) {
                                token.continuePermissionRequest();
                            }
                        }).check()
                }
            }else{
                checkLocation();
            }

        }
    }

    private fun permissionNotFount() {
        showSettingsDialog();
    }

    private fun checkLocation() {

        deviceLocationTracker = DeviceLocationTracker(this, this)
        if(currentlLat==0.0){
            count++;
            alertDialog= LottieAlertDialog.Builder(this,DialogTypes.TYPE_LOADING)
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

    private fun checkLocationFirstTime() {
            val toast = Toast.makeText(
                this,
                "latitude : " + currentlLat + " longitude : " + currentLng,
                Toast.LENGTH_SHORT
            )
            toast.show()
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

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
                openSettings()
            })
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun doesUserHavePermission(): Boolean {
        val result: Int =
            this.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }
}