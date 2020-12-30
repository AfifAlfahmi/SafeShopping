package com.bignerdranch.android.safeshopping

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.Person.fromBundle
import androidx.core.content.ContextCompat
import androidx.media.AudioAttributesCompat.fromBundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.sql.Time

private const val TAG = "ShopsActivity"
private const val FIRST_ITEM = 0

class ShopsActivity : AppCompatActivity() {
    var lat: Double? = null
    var long: Double? = null
    private val locationPermissionCode = 1
    lateinit var locationManager: LocationManager
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    val minDistance:Float = 10f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shops)
        navController = findNavController(R.id.fragment)
        bottomNavigationView = findViewById(R.id.bottmNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        getLocation()
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )

        } else {
            requestLocation()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[FIRST_ITEM]
                == PackageManager.PERMISSION_GRANTED) {
                requestLocation()
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()

        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, minDistance, object :
            LocationListener {
            override fun onLocationChanged(p0: Location) {
                if (p0 != null) {


                    Log.d(TAG, "location changed" + p0.latitude.toString())


                }
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

            override fun onProviderEnabled(p0: String?) {
                TODO("Not yet implemented")
            }

            override fun onProviderDisabled(p0: String?) {
                TODO("Not yet implemented")
            }


        })
        Toast.makeText(this, lat.toString(), Toast.LENGTH_SHORT).show()


        val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        Log.d(TAG, "last location" + localGpsLocation?.latitude.toString())

        if (localGpsLocation != null) {
            lat = localGpsLocation.latitude
            long = localGpsLocation.longitude
            navigateToShopsListFragment()

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragment).navigateUp()
                || super.onSupportNavigateUp()
    }

    fun navigateToShopsListFragment() {


        ShopsListFragment.lat = lat


    }
}