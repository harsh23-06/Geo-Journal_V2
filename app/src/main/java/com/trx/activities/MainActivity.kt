package com.trx.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.trx.R
import com.trx.adapters.MainViewAdapter
import com.trx.database.PlacesDatabase
import com.trx.databinding.ActivityMainBinding
import com.trx.models.PlaceModel
import com.trx.swipe.SwipeToDeleteCallback
import com.trx.swipe.SwipeToEditCallback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var placesList: LiveData<List<PlaceModel>>? = null     //List of Places

    private lateinit var database: PlacesDatabase

    private var fusedLocationClient: FusedLocationProviderClient? = null

    companion object {
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(
            applicationContext,
            PlacesDatabase::class.java,
            "Places_DB"
        ).build()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //Handling the Spinner
        binding.spDistance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            val distanceArray = resources.getStringArray(R.array.Distances_Filter)
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedDistance = distanceArray[position]

                when (selectedDistance) {
                    "All" -> {

                    }

                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }

        binding.btnViewMap.setOnClickListener {
            Intent(this, MapActivity::class.java)
                .putExtra("VIEW", "VIEW_MAP").also {
                    startActivity(it)
                }
        }

        binding.btnAddPlace.setOnClickListener {
            Intent(this, MapActivity::class.java).also {
                it.putExtra("ADD", "ADDON_MAP")
                startActivity(it)
            }
        }
        placesList = database.contactDao().getPlaces()


    }

    private fun getHappyPlacesListFromLocalDB() {
        database = Room.databaseBuilder(
            applicationContext,
            PlacesDatabase::class.java,
            "Places_DB"
        ).build()
        val getPlacesList = database.contactDao().getPlaces()

        if (getPlacesList != null) {
            binding.placesList.visibility = View.VISIBLE
            binding.tvDefaultPlace.visibility = View.GONE
            setupHappyPlacesRecyclerView(getPlacesList)
        } else {
            binding.placesList.visibility = View.GONE
            binding.tvDefaultPlace.visibility = View.VISIBLE
        }
    }

    private fun setupHappyPlacesRecyclerView(happyPlacesList: LiveData<List<PlaceModel>>) {
        binding.placesList.layoutManager = LinearLayoutManager(this)
        binding.placesList.setHasFixedSize(true)

        val placesAdapter = MainViewAdapter(this, fusedLocationClient, happyPlacesList)
        fusedLocationClient?.let { placesAdapter.setCurrentLocation(it) }
        binding.placesList.adapter = placesAdapter

        placesAdapter.setOnClickListener(object :
            MainViewAdapter.OnClickListener {
            override fun onClick(position: Int, model: PlaceModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(
                    EXTRA_PLACE_DETAILS,
                    model
                ) // Passing the complete serializable data class to the detail activity using intent.
                intent.putExtra("SelectedDescription", model.category)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.placesList.adapter as MainViewAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding.placesList)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.placesList.adapter as MainViewAdapter
                adapter.removeAt(viewHolder.adapterPosition)

//                getHappyPlacesListFromLocalDB() // Gets the latest list from the local database after item being delete from it.
                getHappyPlacesListFromLocalDB() // Gets the latest list from the local database after item being delete from it.
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.placesList)
    }

}
