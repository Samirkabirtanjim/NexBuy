package com.example.nexbuy.view.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nexbuy.R
import com.example.nexbuy.databinding.ActivityRegistrationBinding
import com.example.nexbuy.view.login.login
import com.example.nexbuy.view.mainactivity.MainActivity
import com.example.nexbuy.viewmodel.repo.AuthViewModel

class Registration : AppCompatActivity() {

    private lateinit var Binding: ActivityRegistrationBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        Binding.registerCancelButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        Binding.registerButton.setOnClickListener {
            handleRegistration()
        }

    }
    private fun handleRegistration() {
        val name = Binding.name.text.toString()
        val phone = Binding.phone.text.toString()
        val email = Binding.email.text.toString()
        val password = Binding.password.text.toString()

        // Validation for empty fields
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Call the ViewModel's registerUser function
        authViewModel.registerUser(name, phone, email, password) { isSuccess, message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (isSuccess) {
                // On successful registration, navigate to Login page
                val intent = Intent(this, login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}