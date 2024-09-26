package com.example.nexbuy.view.mainactivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.nexbuy.R
import com.example.nexbuy.view.fragments.buy_fragment
import com.example.nexbuy.view.fragments.main_fragment
import com.example.nexbuy.view.fragments.more_fragment
import com.example.nexbuy.view.fragments.offer_fragment
import com.example.nexbuy.view.fragments.sell_fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class home : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.home -> main_fragment()
                R.id.sell -> sell_fragment()
                R.id.buy -> buy_fragment()
                R.id.offer -> offer_fragment()
                R.id.more -> more_fragment()
                else -> throw IllegalArgumentException("Unexpected item ID")
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            true
        }
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.home
        }
    }
}