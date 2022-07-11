package com.example.satellite_predictor.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.satellite_predictor.R
import com.example.satellite_predictor.databinding.ActivityMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var client: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportMapFragment =
            (supportFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment?)!!
        client = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                44
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 44) if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        client.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                supportMapFragment.getMapAsync { googleMap ->
                    val address: String = getAddress(location.latitude, location.longitude)
                    val latLng = LatLng(location.latitude, location.longitude)
                    val bitmapdraw = resources.getDrawable(R.drawable.location) as BitmapDrawable
                    val bitmap = bitmapdraw.bitmap
                    val marker = Bitmap.createScaledBitmap(bitmap, 150, 150, false)
                    val options = MarkerOptions().position(latLng).title(address)
                        .icon(BitmapDescriptorFactory.fromBitmap(marker))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    googleMap.addMarker(options)
                    googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
            }
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        var ans = ""
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addresses[0].getAddressLine(0)
            val city = addresses[0].locality
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            val postalCode = addresses[0].postalCode
            val knownName = addresses[0].featureName
            ans = "$ans$address,$city,$state,$country,$postalCode,$knownName"
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ans
    }

}