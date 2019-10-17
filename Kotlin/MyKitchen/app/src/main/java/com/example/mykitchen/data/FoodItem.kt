package com.example.mykitchen.data

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import com.example.mykitchen.BR

data class FoodItem(
    val id: Int = 0,
    val foodName: String = "",
    val price: Float = 0.0F,
    val likes: Int = 0,
    val imageId: Int = 0,
    val description: Int = 0
)