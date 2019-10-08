package com.example.medicationassistance.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicationassistance.db.AssistanceDatabase
import com.example.medicationassistance.db.entity.PrescribedMedicine
import com.example.medicationassistance.repository.MedicineRepository
import kotlinx.coroutines.launch

class MedicineViewModel internal constructor(application: Application): AndroidViewModel(application) {
    private val repository: MedicineRepository
    val allMedicines: LiveData<List<PrescribedMedicine>>

    init {
        repository = MedicineRepository.getInstance(AssistanceDatabase.getDatabase(application, viewModelScope).prescribedMedicineDao())
        allMedicines = repository.allMedicines
    }

    fun insert(medicine: PrescribedMedicine) = viewModelScope.launch {
        repository.insert(medicine)
    }
}