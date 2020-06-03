package com.example.petland.locations.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.petland.R
import com.example.petland.locations.enums.PlaceTag
import com.example.petland.locations.model.PetlandLocation
import com.example.petland.pet.Pets
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.parse.Parse
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_health.view.*
import kotlinx.android.synthetic.main.fragment_map.view.*


class MapFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, AdapterView.OnItemSelectedListener{
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var rootView: View
    private var filter : PlaceTag? = null
    private var markers = ArrayList<Marker>()
    private var shownLocation : PetlandLocation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_map, container, false)
        rootView.reviewButton.setOnClickListener { viewReviewActivity(shownLocation) }
        val mapFragment = childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment

        mapFragment.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            map = mMap
            onMapReady(map)
        }

        rootView.locationViewLayout.visibility = View.GONE

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
            }
        }

        createLocationRequest()

        val adapterFilter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, getPlaceTagArray())
        adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView.spinnerLocation.adapter = adapterFilter
        rootView.spinnerLocation.onItemSelectedListener = this

        val bundle = this.arguments
        if (bundle != null) {
            rootView.spinnerLocation.setSelection(2)
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
    fun viewReviewActivity( shownLocation : PetlandLocation?) {
        if (shownLocation != null) {
            val intent = Intent(context, ReviewActivity::class.java).apply {}
            intent.putExtra("Location", shownLocation)
            startActivity(intent)
        }
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
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        map.setOnMapClickListener { rootView.locationViewLayout.visibility = View.GONE }
        setUpMap()
        updateMarkers()
    }

    private fun clearMarkers() {
        for(m in markers) m.remove()
        markers.clear()
    }

    private fun updateMarkers() {
        clearMarkers()
        for (location in PetlandLocation.getAllLocations(filter)) {
            markers.add( map.addMarker(MarkerOptions().position(location.getLatLng()).title(location.getName()).icon(location.getMarkerIcon())))
            markers[markers.lastIndex].tag = location
        }
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
                        REQUEST_CHECK_SETTINGS
                    )
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

    @SuppressLint("SetTextI18n")
    override fun onMarkerClick(marker: Marker): Boolean {
        val location : PetlandLocation = marker.tag as PetlandLocation
        shownLocation = location
        rootView.locationViewLayout.visibility = View.VISIBLE

        rootView.locationName.text = location.getName()
        rootView.locationAddress.text = location.getAddress()
        rootView.locationType.text = getPlaceTagTranslated(location.getPlaceTag())
        rootView.locationType.setCompoundDrawablesWithIntrinsicBounds( location.getIcon(), null, null, null)
        rootView.ratingBar.rating = location.getAverageStars().toFloat()
        rootView.ratingText.text = "(" + String.format("%.2f", location.getAverageStars())  + ")"

        if(location.hasLink()) {
            rootView.locationLink.visibility = View.VISIBLE
            rootView.locationLink.text = location.getLink()
        }
        else {
            rootView.locationLink.visibility = View.GONE
            rootView.locationGuion.visibility = View.GONE
        }

        if(location.hasPhoneNumber()) {
            rootView.locationPhone.visibility = View.VISIBLE
            rootView.locationPhone.text = location.getPhoneNumber().toString()
        }
        else {
            rootView.locationPhone.visibility = View.GONE
            rootView.locationGuion.visibility = View.GONE
        }

        if (location.getPlaceTag() == PlaceTag.VETERINARY) {
            val pet = Pets.getSelectedPet()
            val veterinary = pet.get("veterinarian") as ParseObject
            if (location.objectId == veterinary.objectId) {
                rootView.myVeterinary.visibility = View.VISIBLE
                rootView.selectVeterinary.visibility = View.GONE
            }
            else {
                rootView.selectVeterinary.setOnClickListener { setVeterinary(pet, location, marker) }
                rootView.myVeterinary.visibility = View.GONE
                rootView.selectVeterinary.visibility = View.VISIBLE
            }
        }
        else {
            rootView.myVeterinary.visibility = View.GONE
            rootView.selectVeterinary.visibility = View.GONE
        }

        return false
    }


    private fun getPlaceTagArray() : Array<String?> {
        val array = arrayOfNulls<String>(PlaceTag.values().size + 1)
        array[0] = getString(R.string.all)
        PlaceTag.values().forEachIndexed { index, placeTag ->
            array[index+1] = getPlaceTagTranslated(placeTag)
        }
        return array
    }

    private fun getPlaceTagTranslated(placeTag: PlaceTag) : String {
        return when(placeTag) {
            PlaceTag.HAIRDRESSER -> getString(R.string.hairdresser)
            PlaceTag.VETERINARY -> getString(R.string.veterinary)
            PlaceTag.PARK -> getString(R.string.park)
            PlaceTag.RESTAURANT -> getString(R.string.restaurant)
            PlaceTag.OTHER -> getString(R.string.other)
        }
    }

    private fun getPlaceTagToEnum(value: String) : PlaceTag? {
        when(value) {
            getString(R.string.hairdresser) -> return PlaceTag.HAIRDRESSER
            getString(R.string.veterinary) -> return PlaceTag.VETERINARY
            getString(R.string.park) -> return PlaceTag.PARK
            getString(R.string.restaurant) -> return PlaceTag.RESTAURANT
            getString(R.string.other) -> return PlaceTag.OTHER
        }
        return null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        filter = getPlaceTagToEnum(parent?.getItemAtPosition(position).toString())
        updateMarkers()
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun setVeterinary(pet:ParseObject, location: ParseObject, marker:Marker) {
        pet.put("veterinarian", location)
        pet.save()
        rootView.myVeterinary.visibility = View.VISIBLE
        rootView.selectVeterinary.visibility = View.GONE
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
        @JvmStatic
        fun newInstance() =
            MapFragment().apply {}
    }

}
