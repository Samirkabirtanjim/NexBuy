package com.example.nexbuy.view.flashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nexbuy.R
import com.example.nexbuy.view.mainactivity.MainActivity
import com.example.nexbuy.view.mainactivity.home
import com.example.nexbuy.viewmodel.repo.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class flash : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_flash)

        authViewModel = AuthViewModel(this)

        // 4-second delay using Handler
        Handler(Looper.getMainLooper()).postDelayed({
            // After 4 seconds, check if the token exists
            CoroutineScope(Dispatchers.Main).launch {
                authViewModel.getUserToken { token ->
                    if (!token.isNullOrEmpty()) {
                        // If a token exists, navigate to Home activity
                        val intent = Intent(this@flash, home::class.java)
                        startActivity(intent)
                    } else {
                        // If no token, navigate to Main activity
                        val intent = Intent(this@flash, MainActivity::class.java)
                        startActivity(intent)
                    }
                    finish() // Close SplashActivity
                }
            }
        }, 4000) // Delay for 4 seconds
    }
}