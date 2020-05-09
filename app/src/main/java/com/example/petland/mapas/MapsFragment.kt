package com.example.petland.mapas

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petland.HomePrincipalFragment
import com.example.petland.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.fragment_maps.view.*
import java.util.*


class MapsFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener{
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private var geoPoints:  MutableList<LatLng> = arrayListOf()
    private var zoom = 16f
    private var num: Long = 0
    private lateinit var dateIni: Date
    private lateinit var dateEnd: Date
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         rootView = inflater.inflate(R.layout.fragment_maps, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment
        mapFragment.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            map = mMap
            onMapReady(map)

        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                geoPoints.add(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom))
                drawPolyline()



            }

        }

        rootView.buttonFinalizarPaseo.setOnClickListener { finalizarPaseo() }

        createLocationRequest()
        return rootView
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    geoPoints.add(currentLatLng)
                    zoom = 16f
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom))
                }
            }
        chronometer.format = "%s"
        chronometer.base = SystemClock.elapsedRealtime()
        if (chronometer.base != num) {

            chronometer.base = SystemClock.elapsedRealtime()

        } else {

            chronometer.base = chronometer.base + SystemClock.elapsedRealtime()

        }

        chronometer.start()
        dateIni = Calendar.getInstance().time
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }
    override fun onMapReady(p0: GoogleMap?) {
        map.uiSettings.isZoomControlsEnabled = false
        zoom = map.cameraPosition.zoom
        map.setOnMarkerClickListener(this)
        setUpMap()

    }

  fun finalizarPaseo() {

      val time: Long =
          SystemClock.elapsedRealtime() - chronometer.base
      chronometer.stop()

      dateEnd = Calendar.getInstance().time
      Log.d("tiempo" , time.toString())
      Log.d("dateini" , dateIni.toString())
      Log.d("dateend" , dateEnd.toString())
      val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
      val fragment = HomePrincipalFragment.newInstance()
      transaction.replace(R.id.frameLayout, fragment)
      transaction.commit()
  }
    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this.requireContext())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->

            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(activity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                locationUpdateState = true

                startLocationUpdates()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

     override fun onResume() {
         super.onResume()
         if (!locationUpdateState) {
             startLocationUpdates()
         }
     }
    fun drawPolyline(){
        map.clear()
        val polyLine = PolylineOptions().width(5f).color(Color.BLUE)

        for (z in geoPoints.indices) {
            val point: LatLng = geoPoints[z]
            polyLine.add(point)
        }
        val distance: TextView = rootView.findViewById(R.id.distance)
        distance.text =  String.format("%.2f", (SphericalUtil.computeLength(geoPoints)/1000))
        map.addPolyline(polyLine)
    }

    override fun onMarkerClick(p0: Marker?) = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
        @JvmStatic
        fun newInstance() =
            MapsFragment().apply {}
    }

}
