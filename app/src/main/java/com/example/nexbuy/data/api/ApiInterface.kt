package com.example.nexbuy.data.api

import com.example.nexbuy.data.category.CategoryResponse
import com.example.nexbuy.data.login.login_request
import com.example.nexbuy.data.login.login_response
import com.example.nexbuy.data.registration.registration_request
import com.example.nexbuy.data.registration.registration_response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("user/register")
    fun registerUser(@Body registration: registration_request): Call<registration_response>

    @POST("user/login")
    fun loginUser(@Body loginRequest: login_request): Call<login_response>

    @GET("categories")
    fun getCategory(@Header("Authorization") token: String): Call<CategoryResponse>
}