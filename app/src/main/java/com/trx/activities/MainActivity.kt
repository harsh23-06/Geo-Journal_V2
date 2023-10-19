package com.trx.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trx.R
import com.trx.databinding.ActivityMainBinding
import com.trx.databinding.ActivityMapBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapCall.setOnClickListener {
            Intent(this,MapActivity::class.java).also{
                startActivity(it)
            }
        }

    }
}