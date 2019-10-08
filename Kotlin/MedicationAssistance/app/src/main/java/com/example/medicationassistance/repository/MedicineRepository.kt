package com.example.medicationassistance.repository

import androidx.lifecycle.LiveData
import com.example.medicationassistance.db.dao.PrescribedMedicineDao
import com.example.medicationassistance.db.entity.PrescribedMedicine

class MedicineRepository private constructor(private val prescribedMedicineDao: PrescribedMedicineDao) {

    var allMedicines: LiveData<List<PrescribedMedicine>> = prescribedMedicineDao.getMedicinesList()

    suspend fun insert(prescribedMedicine: PrescribedMedicine){
        prescribedMedicineDao.insert(prescribedMedicine)
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: MedicineRepository? = null

        fun getInstance(medicineDao: PrescribedMedicineDao) =
            instance ?: synchronized(this) {
                instance ?: MedicineRepository(medicineDao).also { instance = it }
            }
    }
}
