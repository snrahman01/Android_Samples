package com.example.mykitchen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mykitchen.data.FoodItem
import com.example.mykitchen.data.FoodItemList

class FoodDetailsViewModel(id: Int):ViewModel() {
    val food: FoodItem = FoodItemList.getItem(id)
}