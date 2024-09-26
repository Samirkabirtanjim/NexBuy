package com.example.nexbuy.viewmodel.repo

import android.content.Context
import com.example.nexbuy.data.api.RetrofitClient
import com.example.nexbuy.data.login.login_request
import com.example.nexbuy.data.login.login_response
import com.example.nexbuy.data.registration.registration_request
import com.example.nexbuy.data.registration.registration_response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val context: Context) {
    private val datastoreManager = DatastoreManager(context)

    // Method to get the user token from DataStore
    fun getUserToken(callback: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            datastoreManager.getUserToken().collect { token ->
                callback(token)
            }
        }
    }

    fun registerUser(name: String, phone: String, email: String, password: String, callback: (Boolean, String) -> Unit) {
        val registration = registration_request(email, name, password, phone)

        RetrofitClient.api.registerUser(registration).enqueue(object :
            Callback<registration_response> {
            override fun onResponse(call: Call<registration_response>, response: Response<registration_response>) {
                val message: String
                val isSuccess: Boolean

                if (response.isSuccessful) {
                    message = response.body()?.message ?: "Registration successful. Please log in."
                    isSuccess = true
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "An unexpected error occurred during registration."
                    message = "Registration failed: $errorMessage"
                    isSuccess = false
                }
                // Pass the success status and message back to the Activity
                callback(isSuccess, message)
            }

            override fun onFailure(call: Call<registration_response>, t: Throwable) {
                callback(false, "Registration failed due to network error: ${t.message}")
            }
        })
    }

    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        val loginUser = login_request(email, password)

        // Log the request to ensure correct data is being sent
        println("Login Request: Email = $email, Password = $password")

        RetrofitClient.api.loginUser(loginUser).enqueue(object : Callback<login_response> {
            override fun onResponse(call: Call<login_response>, response: Response<login_response>) {
                val message: String
                val isSuccess: Boolean

                if (response.isSuccessful) {
                    // Log the successful response for debugging
                    println("Login Response: ${response.body()}")

                    // Store the token in DataStore
                    response.body()?.token?.let { token ->
                        CoroutineScope(Dispatchers.IO).launch {
                            datastoreManager.saveString("user_token", token)
                        }
                    }

                    message = response.body()?.message ?: "Login successful"
                    isSuccess = true
                } else {
                    // Log the error response for debugging
                    println("Login Error: ${response.errorBody()?.string()}")

                    val errorMessage = response.errorBody()?.string() ?: "Invalid credentials"
                    message = "Login failed: $errorMessage"
                    isSuccess = false
                }
                callback(isSuccess, message)
            }

            override fun onFailure(call: Call<login_response>, t: Throwable) {
                // Log the failure response for debugging
                println("Login Failure: ${t.message}")

                callback(false, "Login failed due to network error: ${t.message}")
            }
        })
    }
}