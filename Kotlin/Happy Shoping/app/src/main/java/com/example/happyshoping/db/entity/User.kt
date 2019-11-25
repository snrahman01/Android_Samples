package com.example.happyshoping.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (@PrimaryKey val id: Int,
            @ColumnInfo(name = "full_name") var mTitle: String,
            @ColumnInfo(name = "mbl_no") var mPtice: Float,
            @ColumnInfo(name = "email_id") var mTotalCount: Int,
            @ColumnInfo(name = "address") var mPicId: Int)