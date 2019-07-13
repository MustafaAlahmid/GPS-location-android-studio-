package com.example.locationsnotificater

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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

        loadTarget()
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,15f,myLocation)
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
    var oldLocation:Location?=null
    var location1:Location?=null
    var location2:Location?=null
   inner class MyThread:Thread{
        constructor():super() {
            oldLocation = Location("old loction")
            oldLocation!!.latitude = 0.0
            oldLocation!!.longitude = 0.0

            location1 = Location("city mall")
            location1!!.latitude = 30.300059
            location1!!.longitude = 60.005188

            location2 = Location("LETI")
            location2!!.latitude = 30.324073
            location2!!.longitude = 59.971891

        }


       override fun run() {
           while (true){
               try {
                   if (oldLocation!!.distanceTo(location1) == 100f){
                       val intent = Intent()
                       val pendingIntent = PendingIntent.getActivity(this@MapsActivity,0,intent,0)
                       val notification = Notification.Builder(this@MapsActivity)
                           .setSmallIcon(R.drawable.notification_icon_background)
                           .setContentTitle("Target is near")
                           .setContentText("You Are Near City Mall")
                       notification.setContentIntent(pendingIntent)

                       val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                       notificationManager.notify(0,notification.build())

                   }
                   if (oldLocation!!.distanceTo(mylocation) == 0f){
                       continue
                   }
                   oldLocation = mylocation

                   runOnUiThread {
                   mMap.clear()
                   // Add a marker in Sydney and move the camera
                   val sydney = LatLng(mylocation!!.latitude, mylocation!!.longitude)
                   mMap.addMarker(MarkerOptions().position(sydney).title("ME"))
                   mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


                       // show the targets on the map
                       for (i in 0..listOfTargets.size-1){
                            var Target = listOfTargets[i]
                            if (Target.isNear==false) {
                                val target = LatLng(Target.location!!.longitude, Target.location!!.latitude)
                                mMap.addMarker(MarkerOptions().position(target).title(Target.name))
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


                            }

                       }
               }

                   Thread.sleep(1000)
               }catch (ex:Exception){}
           }

       }

   }

    var listOfTargets = ArrayList<target>()
    fun loadTarget(){
        listOfTargets.add(target("Leti",59.971891,30.324073,false))
        listOfTargets.add(target("city Mall",60.005188,30.300059,false))
    }
}



/*
 /// notify me if the distance is less than 10000f
                                var tarLoc :Location?=null
                                tarLoc = Location(tarLoc)
                                tarLoc!!.latitude = target.latitude
                                tarLoc!!.longitude = target.longitude
                                if (mylocation!!.distanceTo(tarLoc) <= 5000f){
                                    val intent = Intent()
                                    val pendingIntent = PendingIntent.getActivity(this@MapsActivity,0,intent,0)
                                    val notification = Notification.Builder(this@MapsActivity)
                                        .setSmallIcon(R.drawable.notification_icon_background)
                                        .setContentTitle("Target is near")
                                        .setContentText("You Are Near ${Target.name}")
                                    notification.setContentIntent(pendingIntent)

                                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                    notificationManager.notify(0,notification.build())


                                }


 */