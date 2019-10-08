package com.example.medicationassistance.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medicationassistance.db.dao.PrescribedMedicineDao
import com.example.medicationassistance.db.entity.PrescribedMedicine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [PrescribedMedicine::class], version = 1)
abstract class AssistanceDatabase: RoomDatabase(){

    abstract fun prescribedMedicineDao(): PrescribedMedicineDao

    private class AssistanceDatabaseCallback(private val scope: CoroutineScope
    ): RoomDatabase.Callback(){

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let {assistanceDatabase ->
                scope.launch {
                    var  prescribedMedicineDao = assistanceDatabase.prescribedMedicineDao()

                    prescribedMedicineDao.deleteAll()

                    var medicine = PrescribedMedicine("Maximus", "tetsss", 3, true)
                    prescribedMedicineDao.insert(medicine)

                    medicine = PrescribedMedicine("Tufnil", "tr", 2, true)
                    prescribedMedicineDao.insert(medicine)
                }
            }
        }
    }

    companion object{

        @Volatile
        private var INSTANCE: AssistanceDatabase?=null

        fun getDatabase(context: Context, scope: CoroutineScope
        ): AssistanceDatabase{

            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AssistanceDatabase::class.java,
                    "assistance_database"
                ).addCallback(AssistanceDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}