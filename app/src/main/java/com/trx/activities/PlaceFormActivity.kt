package com.trx.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trx.R
import com.trx.database.placesDatabase
import com.trx.databinding.ActivityMainBinding
import com.trx.databinding.ActivityPlaceFormBinding

class PlaceFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceFormBinding
    private lateinit var database: placesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.pfToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        database = Room.databaseBuilder(
            applicationContext,
            placesDatabase::class.java,
            "PlacesDB"
        ).build()



    }
}