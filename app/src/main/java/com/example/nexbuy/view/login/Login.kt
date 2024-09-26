package com.example.nexbuy.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nexbuy.databinding.ActivityLoginBinding
import com.example.nexbuy.view.mainactivity.MainActivity
import com.example.nexbuy.view.mainactivity.home
import com.example.nexbuy.viewmodel.repo.AuthViewModel


class login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = AuthViewModel(this) // Initialize ViewModel

        binding.loginButton.setOnClickListener {
            handleLogin() // Call the login handler when the login button is clicked
        }
    }
    private fun handleLogin() {
        val email = binding.email.text.toString().trim() // Retrieve email from the input field
        val password = binding.password.text.toString().trim() // Retrieve password from the input field

        // Validate user input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Call the ViewModel to handle login
        authViewModel.loginUser(email, password) { isSuccess, message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (isSuccess) {
                val intent = Intent(this, home::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}