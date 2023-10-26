package com.trx.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.room.Room
import com.trx.database.PlacesDatabase
import com.trx.databinding.ActivityPlaceFormBinding
import com.trx.models.PlaceModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PlaceFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceFormBinding

    //initializing our database
    private lateinit var database: PlacesDatabase

    //Properties of our place object
    private lateinit var title: String
    private lateinit var date: String
    private var latitude: Double = 0.00
    private var longitude: Double = 0.00
    private lateinit var address: String
    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting up toolbar
        setSupportActionBar(binding.pfToolbar)
        supportActionBar?.title = "Add place"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val currentDate = getCurrentDate()
        binding.date.text = "Date : $currentDate"

        database = Room.databaseBuilder(
            applicationContext,
            PlacesDatabase::class.java,
            "PlacesDB"
        ).build()

        //if the intent is coming from the draggable marker
        if (intent.hasExtra("DRAG_LATITUDE") && intent.hasExtra("DRAG_LONGITUDE") &&
            intent.hasExtra("DRAG_ADDRESS")
        ) {

            try {
                latitude = intent.getDoubleExtra("DRAG_LATITUDE", 0.0)
                longitude = intent.getDoubleExtra("DRAG_LONGITUDE", 0.0)
                address = intent.getStringExtra("DRAG_ADDRESS").toString()
                binding.tvAddress.text = address
            } catch (e: Exception) {
                Toast.makeText(
                    this, "Some error in fetching place",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        //Handling click on radio Buttons
        binding.rbgCategory.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbCommercial.id -> {
                    category = "COMMERCIAL"
                }

                binding.rbResidential.id -> {
                    category = "RESIDENTIAL"
                }
            }
        }

        //Handling date text View
        binding.date.setOnClickListener {
            showDatePickerDialog()
        }

        //Adding the place to the database
        binding.btnAdd.setOnClickListener {

            //fields validation
            if (binding.tvTitle.text.isEmpty() || category.isEmpty() ||
                binding.tvAddress.text.isEmpty()
            ) {
                Toast.makeText(
                    this, "Fields cannot be Empty",
                    Toast.LENGTH_SHORT
                ).show()
            }

            title = binding.tvTitle.text.toString()
            date = binding.date.text.toString()

            val placeObj = PlaceModel(
                0,
                title,
                category,
                date,
                address,
                latitude,
                longitude
            )

            GlobalScope.launch {
                database.contactDao().insertPlace(placeObj)
            }

            Toast.makeText(this, "Place Inserted",
                Toast.LENGTH_SHORT).show()

            Intent(this,MainActivity::class.java).also {
                startActivity(it)
            }
        }

    }

    //for getting the current date
    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    //for showing the date picker dialog after click
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

    //After selecting date from dialog and setting it in text view
    private fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month)
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = dateFormat.format(selectedDate.time)

        binding.date.text = "Date : $formattedDate"
    }


}