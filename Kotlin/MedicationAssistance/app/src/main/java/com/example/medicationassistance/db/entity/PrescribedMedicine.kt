package com.example.medicationassistance.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prescribedmedicine_table")
data class PrescribedMedicine(@PrimaryKey
                        @ColumnInfo(name = "medi_name") var mMediName: String,
                        @ColumnInfo(name = "medi_des") var mMediDes: String,
                        @ColumnInfo(name = "medi_freq") var mMediFreq: Int,
                        @ColumnInfo(name = "food_interaction") var mFoodInteraction: Boolean)