package com.trx.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import com.trx.R
import com.trx.database.PlacesDatabase
import com.trx.databinding.ActivityMainBinding
import com.trx.models.PlaceModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var placesList : LiveData<List<PlaceModel>>? = null     //List of Places

    private lateinit var database : PlacesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Instantiating the Database
        database = PlacesDatabase.getInstance(applicationContext)

        //Getting all the places
        placesList = database.contactDao().getPlaces()

        //Handling the Spinner
        binding.spDistance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            val distanceArray = resources.getStringArray(R.array.Distance_Filter)
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

        //Handling the View Map button
        binding.btnViewMap.setOnClickListener {
            Intent(this,MapActivity::class.java)
                .putExtra("VIEW","VIEW_MAP").also{
                startActivity(it)
            }
        }

        //Handling the ADD Button
        binding.btnAddPlace.setOnClickListener{
            Intent(this,MapActivity::class.java).also {
                it.putExtra("ADD","ADDON_MAP")
                startActivity(it)
            }
        }

    }
}