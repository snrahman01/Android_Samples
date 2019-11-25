package com.example.happyshoping.db.entity

import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item (@PrimaryKey val id: Int,
                 @ColumnInfo(name = "item_title") var mTitle: String,
                 @ColumnInfo(name = "unit_price") var mPrice: Float,
                 @ColumnInfo(name = "total_count") var mTotalCount: Int,
                 @ColumnInfo(name = "pic_id") var mPicId: Int,
                 @ColumnInfo(name = "sold_count") var mSoldCount: Int)