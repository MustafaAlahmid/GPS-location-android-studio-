package com.example.locationsnotificater

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getPermission()
    }
    val accessLocation = 123
    fun getPermission(){
        if (Build.VERSION.SDK_INT >= 23){

            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),accessLocation)
                return
            }
        }

        getUserLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            accessLocation->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getUserLocation()
                }else{
                    Toast.makeText(this,"access denied ",Toast.LENGTH_LONG).show()
                }
            }


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getUserLocation(){
        Toast.makeText(this,"now access location",Toast.LENGTH_LONG).show()

        val myLocation = myLocationListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        val myThread = MyThread()
        myThread.start()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap




    }
    var mylocation:Location?=null
   inner class myLocationListener:LocationListener{
        constructor(){
            mylocation = Location("me")
            mylocation!!.latitude = 0.0
            mylocation!!.longitude = 0.0
        }
        override fun onLocationChanged(location: Location?) {
            mylocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }


    }
   inner class MyThread:Thread{
        constructor():super() {
        }

       override fun run() {
           while (true){
               try {



                   runOnUiThread {
                   mMap.clear()
                   // Add a marker in Sydney and move the camera
                   val sydney = LatLng(mylocation!!.latitude, mylocation!!.longitude)
                   mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10f))
               }

                   Thread.sleep(1000)
               }catch (ex:Exception){}
           }

       }

   }
}

