package com.richard.minnyadagboksapp // <-- KONTROLLERA ATT DETTA ÄR DITT PACKAGE NAME

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.richard.minnyadagboksapp.databinding.ActivityAddEntryBinding // <-- KONTROLLERA DETTA OCKSÅ

class AddEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Använd View Binding för att sätta upp layouten
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Här kommer vi lägga till logik för knapparna senare
    }
}