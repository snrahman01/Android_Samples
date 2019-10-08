package com.example.medicationassistance.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medicationassistance.db.entity.PrescribedMedicine

@Dao
interface PrescribedMedicineDao {

    @Query("SELECT * FROM prescribedmedicine_table")
    fun getMedicinesList(): LiveData<List<PrescribedMedicine>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(medicine: PrescribedMedicine)

    @Query("DELETE FROM prescribedmedicine_table")
    suspend fun deleteAll()
}