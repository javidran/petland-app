package com.example.petland.mapas

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
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
import com.example.petland.HomeActivity
import com.example.petland.R
import com.example.petland.events.enums.FilterEvent
import com.example.petland.events.model.PetEvent
import com.example.petland.pet.Pet
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
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.fragment_maps.view.*
import java.text.SimpleDateFormat
import java.util.*


class MapsFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private var geoPoints: MutableList<LatLng> = arrayListOf()
    private var zoom = 16f
    private var num: Long = 0
    private lateinit var listPets: Array<String>
    private lateinit var dateIni: Date
    private lateinit var dateEnd: Date
    private lateinit var rootView: View
    private lateinit var distance: TextView
    private lateinit var listObjectsPet: List<Pet>
    var selection = ""
    var time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

        createDialogPets()

    }

    private fun createDialogPets() {
        val selectedItems = ArrayList<Pet>()
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.walkpet))
        listObjectsPet = Pet.getPetsFromCurrentUser()
        listPets = Pet.getNamesFromPetList(listObjectsPet)
        val listObjectId = Pet.getIdFromPetsList(listObjectsPet)
        val num: Int = listObjectId.indexOf(Pet.getSelectedPet().objectId)
        val checkedItems = BooleanArray(listPets.size)
        checkedItems[num] = true
        selectedItems.add(listObjectsPet[num])
        builder.setMultiChoiceItems(listPets, checkedItems) { dialog, which, isChecked ->
            if (isChecked) {
                selectedItems.add(listObjectsPet[which])
            } else if (selectedItems.contains(listObjectsPet[which])) {
                selectedItems.removeAt(selectedItems.indexOf(listObjectsPet[which]))
            }
        }

        builder.setPositiveButton("OK") { dialog, which ->

            listObjectsPet = selectedItems
            if (listObjectsPet.isEmpty()) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage(getString(R.string.listNotNull))
                builder.setPositiveButton("OK") { dialog, which ->
                }
                createDialogPets()
                val dialog = builder.create();
                dialog.show()

            }

        }

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()


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
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                drawPolyline()
            }

        }

        rootView.buttonFinalizarPaseo.setOnClickListener { endWalkConfirmation() }

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
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    geoPoints.add(currentLatLng)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
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

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    override fun onMapReady(p0: GoogleMap?) {
        map.uiSettings.isZoomControlsEnabled = false
        zoom = map.cameraPosition.zoom
        map.setOnMarkerClickListener(this)
        setUpMap()

    }

    fun endWalkConfirmation() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.end_walk))
        builder.setMessage(getString(R.string.end_walk_message))

        builder.setPositiveButton((getString(R.string.ok))) { dialog, which ->
            ShowEventDialog()

        }
        builder.setNeutralButton(getString(R.string.cancel)) { _, _ ->
        }
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun ShowEventDialog() {
        selection = ""
        time = SystemClock.elapsedRealtime() - chronometer.base
        chronometer.stop()
        dateEnd = Calendar.getInstance().time
        for (pets in listObjectsPet) {
            val query = ParseQuery.getQuery<Pet>(Pet::class.java)
            query.whereEqualTo("objectId", pets.objectId)
            val result = query.find().first()
            val listEventsPet = ArrayList<PetEvent>()
            val petEvents = PetEvent.getEventsFromPet(result, FilterEvent.ONLY_WALK)

            for (event in petEvents) {
                val sdf = SimpleDateFormat("dd/MM/yy", Locale.US)
                if (sdf.format(event.getDate()) == sdf.format(dateIni) && !event.isDone()) {
                    listEventsPet.add(event)
                }
            }
            if (listEventsPet.isNotEmpty()) {
                val dateEvents = ArrayList<String>()
                for (event in listEventsPet) {
                    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.US)
                    dateEvents.add(sdf.format(event.getDate()))
                }
                dateEvents.add(getString(R.string.noAssignedToEvent))
                val builder = AlertDialog.Builder(context)
                builder.setTitle(getString(R.string.walkev) + pets.getName() +  " " + getString(R.string.eventPet))
                builder.setItems(dateEvents.toArray(arrayOfNulls<String>(0))) { dialog, which ->
                    selection = dateEvents[which]
                    val walk = Walk()
                    walk.createWalk(dateEnd, dateIni, time.toInt(),
                        distance.text as String, getLatitudes(),getLongitudes(),selection,listEventsPet,pets)
                }
                val dialog = builder.create()
                dialog.setCanceledOnTouchOutside(false)
                dialog.setCancelable(false)
                dialog.show()
            }
            else {
                val walk = Walk()
                walk.createWalk(dateEnd, dateIni, time.toInt(),
                    distance.text as String, getLatitudes(),getLongitudes(),selection,listEventsPet, pets)
            }
        }
        changeToHomePrincipalFragment()
    }


    fun changeToHomePrincipalFragment() {
        (activity as HomeActivity).volverHome()
    }


    private fun getLatitudes(): MutableList<Double> {
        val latitudes: MutableList<Double> = arrayListOf()

        for (z in geoPoints.indices) {
            val point = geoPoints[z].latitude
            latitudes.add(point)
        }
        return latitudes
    }

    private fun getLongitudes(): MutableList<Double> {
        val longitudes: MutableList<Double> = arrayListOf()

        for (z in geoPoints.indices) {
            val point = geoPoints[z].longitude
            longitudes.add(point)
        }
        return longitudes
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
                    e.startResolutionForResult(
                        activity,
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
        if (locationUpdateState) {
            onMapReady(map)
            startLocationUpdates()
        }
    }

    fun drawPolyline() {
        map.clear()
        val polyLine = PolylineOptions().width(5f).color(R.color.colorAccent)

        for (z in geoPoints.indices) {
            val point: LatLng = geoPoints[z]
            polyLine.add(point)
        }
        distance = rootView.findViewById(R.id.distance)
        distance.text = String.format("%.2f", (SphericalUtil.computeLength(geoPoints) / 1000))
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
