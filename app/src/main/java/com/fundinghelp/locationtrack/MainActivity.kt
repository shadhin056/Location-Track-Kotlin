package com.fundinghelp.locationtrack

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.net.Uri
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
                        if (response.isPermanentlyDenied()) {
                             permissionNotFount();
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown( permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest();
                    }
                }).check()
        }
        }

    private fun permissionNotFount() {
        showSettingsDialog();
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
}