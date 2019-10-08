package com.example.medicationassistance.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicationassistance.repository.MedicineRepository

class MedicineViewModelFactory(
    private val application: Application) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>)=MedicineViewModel(application) as T
}