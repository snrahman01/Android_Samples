package com.example.mykitchen.viewmodel

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.mykitchen.data.FoodItem
import com.example.mykitchen.R
import com.example.mykitchen.data.FoodItemList

class FoodItemViewModel: ViewModel() {

    var foodItems: List<FoodItem> = FoodItemList.getItems()
}