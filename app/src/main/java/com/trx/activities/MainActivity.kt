package com.trx.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.trx.R
import com.trx.databinding.ActivityMainBinding
import com.trx.databinding.ActivityMapBinding
import com.trx.models.PlaceModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var placesList : ArrayList<PlaceModel>? = null     //List of Places

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.btnViewMap.setOnClickListener {
            Intent(this,MapActivity::class.java).also{
                startActivity(it)
            }
        }

    }
}