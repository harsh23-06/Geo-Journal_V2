package com.trx.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.trx.R
import com.trx.database.placesDatabase
import com.trx.databinding.ActivityMainBinding
import com.trx.databinding.ActivityMapBinding
import com.trx.models.PlaceModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var placesList : LiveData<List<PlaceModel>>? = null     //List of Places

    private lateinit var database : placesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(applicationContext,
            placesDatabase::class.java,
            "Places_DB").build()

        //Handling the Spinner
        binding.spDistance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            val distanceArray = resources.getStringArray(R.array.Distances_Filter)
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDistance = distanceArray[position]

                when(selectedDistance){
                    "All" -> {

                    }

                }

            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }

        binding.btnViewMap.setOnClickListener {
            intent.putExtra("VIEW","VIEW_MAP")
            Intent(this,MapActivity::class.java).also{
                startActivity(it)
            }
        }

        binding.btnAddPlace.setOnClickListener{
            intent.putExtra("ADD","ADDON_MAP")
            Intent(this,MapActivity::class.java).also {
                startActivity(it)
            }
        }
        placesList = database.contactDao().getPlaces()


    }
    private fun getHappyPlacesListFromLocalDB() {
//        val dbHandler = DatabaseHandler(this)
//        val getHappyPlacesList = dbHandler.getHappyPlacesList()

        if (getHappyPlacesList.size > 0) {
            binding.placesList.visibility = View.VISIBLE
            binding.tvDefaultPlace.visibility = View.GONE
            setupHappyPlacesRecyclerView(getHappyPlacesList)
        } else {
            binding.placesList.visibility = View.GONE
            binding.tvDefaultPlace.visibility = View.VISIBLE
        }
    }

    private fun setupHappyPlacesRecyclerView(happyPlacesList: PlaceModel) {

    }
}