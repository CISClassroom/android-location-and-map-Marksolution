package com.cis.google_map_api

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var locationClient : FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    var lat:Double = 0.0
    var lon:Double = 0.0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_maps)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(lat,lon)
        mMap.addMarker(MarkerOptions().position(sydney).title("Your location!"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun getLocation() {
        if(checkPermission()){
            locationClient.lastLocation.addOnCompleteListener(this) {task ->
                var location : Location? = task.result
                if(location != null){
                    lat = location.latitude.toDouble() ;
                    lon = location.longitude.toDouble();
                    onMapReady(mMap)
                    //Log.i("Location",lat+","+lon)
                    //textView.text = lat+","+lon;
                }
            }
        }else{
            requestPermissions()
            Log.i("Location","null location")
        }
    }
    private fun checkPermission(): Boolean{
        if(ActivityCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_COARSE_LOCATION
            )== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,Manifest.permission.ACCESS_FINE_LOCATION
            )== PackageManager.PERMISSION_GRANTED
            ){
            return true
        }
        return false
    }
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            101
        )
    }
}
