package com.example.medicationassistance.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medicationassistance.db.AssistanceDatabase
import com.example.medicationassistance.db.entity.PrescribedMedicine
import com.example.medicationassistance.repository.MedicineRepository
import kotlinx.coroutines.launch

class AddMedicineViewModel internal constructor(application: Application): AndroidViewModel(application) {
    private val repository: MedicineRepository
    init {
        repository = MedicineRepository.getInstance(AssistanceDatabase.getDatabase(application, viewModelScope).prescribedMedicineDao())
    }

    fun insert(medicine: PrescribedMedicine) = viewModelScope.launch {
        repository.insert(medicine)
    }
}