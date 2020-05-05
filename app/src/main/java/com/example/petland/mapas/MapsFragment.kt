package com.example.petland.mapas

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions


class MapsFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener{
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val mLocationCallback: LocationCallback? = null
    private var geoPoints =  listOf<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
    }
    interface LocationCallback {
        fun handleNewLocation(location: Location?)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment
        mapFragment.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            map = mMap
            onMapReady(map)

        }
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
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
    }


    override fun onMapReady(p0: GoogleMap?) {
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        setUpMap()
        //drawTestPolilyne();
    }
    /*
    fun drawTestPolilyne(){
        val line = map.addPolyline(
            PolylineOptions()
                .add(LatLng(51.5, -0.1), LatLng(40.7, -74.0))
                .width(5f)
                .color(Color.RED)
        )
    }
    */

    fun drawPolyline(){
        map.clear()
        val polyLine = PolylineOptions().width(5f).color(Color.BLUE)
        for (z in geoPoints.indices) {
            val point: LatLng = geoPoints.get(z)
            polyLine.add(point)
        }
        map.addPolyline(polyLine)
    }

    fun onLocationChanged(location: Location?) {
        mLocationCallback?.handleNewLocation(location)

        val text: CharSequence = "New locationa added"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)
        toast.show()
        drawPolyline()
    }
    override fun onMarkerClick(p0: Marker?) = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        @JvmStatic
        fun newInstance() =
            MapsFragment().apply {}
    }

}
