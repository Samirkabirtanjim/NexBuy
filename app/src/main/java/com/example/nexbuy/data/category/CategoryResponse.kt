package com.example.nexbuy.data.category

data class CategoryResponse(
    val status: Int,
    val data: List<Category>
)
data class Category(
    val id: Int,
    val name: String,
    val priority: Any?,
    val status: Int,
    val sub_categories: List<SubCategory>
)

data class SubCategory(
    val id: Int,
    val category_id: Int,
    val name: String,
    val priority: Any?,
    val status: Int
)
