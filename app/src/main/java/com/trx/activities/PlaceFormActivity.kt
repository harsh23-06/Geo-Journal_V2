package com.trx.activities

import android.app.DatePickerDialog
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trx.R
import com.trx.database.placesDatabase
import com.trx.databinding.ActivityMainBinding
import com.trx.databinding.ActivityPlaceFormBinding
import com.trx.models.PlaceModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PlaceFormActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPlaceFormBinding
    private lateinit var database: placesDatabase
    private var mLatitude: Double = 0.0 // A variable which will hold the latitude value.
    private var mLongitude: Double = 0.0 // A variable which will hold the longitude value.
    private var mTitle = ""
    private var mPlaceDetails: PlaceModel? = null

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

        //date will be autoSet when the activity is created
        val currentDate = getCurrentDate()
        binding.date.setText(currentDate)

        //intent to edit list item
        if(intent.hasExtra("Item")){
            mPlaceDetails = intent.getSerializableExtra("Item") as PlaceModel
        }
        //if edit is asked
        if(mPlaceDetails != null){
            supportActionBar?.title = "Edit Place"

            binding.title.setText(mPlaceDetails!!.title)
            binding.description.text =mPlaceDetails!!.category
            binding.date.setText(mPlaceDetails!!.date)
            binding.location.text = mPlaceDetails!!.location
            mLatitude = mPlaceDetails!!.latitude
            mLongitude = mPlaceDetails!!.longitude
        }

        //intent from places fragment
        if(intent.hasExtra("Add") and intent.hasExtra("Lat") and intent.hasExtra("Long")){

        }
        //intent from draggable marker
        if(intent.hasExtra("DRAG_LATITUDE") and intent.hasExtra("DRAG_LONGITUDE") and intent.hasExtra("DRAG_TITLE")){

        }

        binding.date.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.date ->{
                showDatePickerDialog()
            }

        }
    }


    //after clicking on date ediText
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                // Handle the selected date
                onDateSet(year, month, dayOfMonth)
            },
            currentYear,
            currentMonth,
            currentDay
        )



        datePickerDialog.show()
    }
    //date will set after date is picked
    private fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month)
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = dateFormat.format(selectedDate.time)

        // Update the date with the selected date
        binding.date.setText(formattedDate)
    }
    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(latitude, longitude, 1)
        return list!![0].getAddressLine(0)
    }

    //auto set date
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
    private fun swipeCallBack(){

    }

}
