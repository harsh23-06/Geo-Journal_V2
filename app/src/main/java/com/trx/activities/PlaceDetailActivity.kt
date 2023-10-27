package com.trx.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trx.R
import com.trx.databinding.ActivityPlaceDetailBinding

class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting up toolbar
        setSupportActionBar(binding.toolbar.customToolbar)
        //TODO : (Have to set it to the item's title)
        supportActionBar?.title = "Information"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



    }
}