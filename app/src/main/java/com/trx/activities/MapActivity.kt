package com.trx.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.trx.R
import com.trx.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    //OnMapReadyCallback interface for implementing google maps

    //Class Members
    private lateinit var binding : ActivityMapBinding    //for view binding
    private var mGoogleMap : GoogleMap? = null      //for initializing google map
    private lateinit var autoCompleteFragment : AutocompleteSupportFragment  //auto complete search

    //Current location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //<----------For adding Google Map---------->
        // can use id with view binding like this
        val mapFragment = supportFragmentManager.findFragmentById(binding?.mapFragment!!.id)
                as SupportMapFragment
        mapFragment.getMapAsync(this)
        //<----------end----------------------------->


        // <----------Google Autocomplete search from places API------------->
        Places.initialize(applicationContext,getString(R.string.google_maps_api_key))
        //normal use of layout id
        autoCompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment
        autoCompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))

        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(place: Status) {
                Toast.makeText(this@MapActivity, "Some Error in Search",
                    Toast.LENGTH_SHORT).show()
            }
            override fun onPlaceSelected(place: Place) {
                val add = place.address
                val id = place.id

                val latLng = place.latLng
            }
        })
        //<-------------Autocomplete Search ends here----------------->


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        //for zoom on current location
        mGoogleMap?.uiSettings?.isZoomControlsEnabled = true
        setupMap()

    }

    private fun setupMap() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //TODO : Have to request permission from user
            return
        }
        mGoogleMap?.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if(location != null){
                val currentLatLang = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLang)
                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLang,15f))
            }
        }

    }

    private fun placeMarkerOnMap(position : LatLng){
        val marker = MarkerOptions().position(position)
        marker.title("$position")
        marker.draggable(true)
        mGoogleMap?.addMarker(marker)
    }

    //showing details on marker click
    override fun onMarkerClick(place: Marker) = false

}