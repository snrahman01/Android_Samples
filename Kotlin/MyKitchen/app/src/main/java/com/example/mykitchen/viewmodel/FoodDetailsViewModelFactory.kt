package com.example.mykitchen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FoodDetailsViewModelFactory(
    private val id:Int
):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FoodDetailsViewModel(id) as T
    }
}