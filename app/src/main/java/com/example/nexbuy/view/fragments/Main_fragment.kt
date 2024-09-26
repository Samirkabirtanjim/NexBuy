package com.example.nexbuy.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.nexbuy.R
import com.example.nexbuy.data.api.RetrofitClient
import com.example.nexbuy.data.category.Category
import com.example.nexbuy.data.category.CategoryAdapter
import com.example.nexbuy.data.category.CategoryResponse
import com.example.nexbuy.viewmodel.repo.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class main_fragment : Fragment() {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryList = mutableListOf<Category>()
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)
        val imageList = ArrayList<SlideModel>().apply {
            add(SlideModel(R.drawable.oneads, scaleType = ScaleTypes.CENTER_CROP))
            add(SlideModel(R.drawable.twoads, scaleType = ScaleTypes.CENTER_CROP))
            add(SlideModel(R.drawable.threeads, scaleType = ScaleTypes.CENTER_CROP))
            add(SlideModel(R.drawable.fourads, scaleType = ScaleTypes.CENTER_CROP))
        }
        imageSlider.setImageList(imageList)

        categoryRecyclerView = view.findViewById(R.id.category)
        categoryAdapter = CategoryAdapter(categoryList)
        categoryRecyclerView.adapter = categoryAdapter
        categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Initialize AuthViewModel
        authViewModel = AuthViewModel(requireContext())

        // Fetch categories
        fetchCategories()

        return view
    }

    private fun fetchCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            authViewModel.getUserToken { token ->
                token?.let {
                    // Pass the token to the API call
                    RetrofitClient.api.getCategory(it).enqueue(object : Callback<CategoryResponse> {
                        override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                            if (response.isSuccessful) {
                                response.body()?.let { categoryResponse ->
                                    categoryList.clear()
                                    categoryList.addAll(categoryResponse.data)
                                    categoryAdapter.notifyDataSetChanged()
                                } ?: run {
                                    Toast.makeText(context, "No categories found", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Log.e("API_ERROR", "Code: ${response.code()}, Message: ${response.message()}")
                                Toast.makeText(context, "Failed to load categories: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                            Log.e("API_ERROR", "Failure: ${t.message}")
                            Toast.makeText(context, "Error fetching categories: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } ?: run {
                    Log.e("TOKEN_ERROR", "No token found.")
                    Toast.makeText(context, "No token found. Please log in again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
