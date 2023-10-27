package com.trx.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trx.R
import com.trx.databinding.ActivityHappyPlaceDetailBinding
import com.trx.models.PlaceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.URL

class HappyPlaceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHappyPlaceDetailBinding
    private var detailPlaceModel : PlaceModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //intent from MainActivity
        if(intent.hasExtra("MainActivityIntent")){
            //though getSerializable and getParcelable is deprecated, no other function found
            detailPlaceModel = intent.getSerializableExtra("MainActivityIntent") as PlaceModel
        }

        //update the actionBar of the activity
        if(detailPlaceModel != null){
            setSupportActionBar(binding.toolbarHappyPlaceDetail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = detailPlaceModel!!.title
            binding.toolbarHappyPlaceDetail.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            setImage(detailPlaceModel!!)

            binding.tvDescription.text = detailPlaceModel!!.category
            binding.tvLocation.text = detailPlaceModel!!.address

        }

    }
    //for displaying image of the location
    private fun setImage(model : PlaceModel){

        val latitude = model.latitude
        val longitude = model.longitude

        val zoom = 15
        val imageSize = "400x400"
        val staticMapUrl = "https://maps.googleapis.com/maps/api/staticmap" +
                "?center=$latitude,$longitude" +
                "&zoom=$zoom" +
                "&size=$imageSize" +
                "&key=${getString(R.string.google_maps_api_key)}"
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream: InputStream = URL(staticMapUrl).openStream()
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

                runOnUiThread {
                    binding.ivPlaceImage.setImageBitmap(bitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }
}