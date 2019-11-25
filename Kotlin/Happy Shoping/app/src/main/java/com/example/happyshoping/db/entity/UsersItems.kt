package com.example.happyshoping.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "users_items_table",
    primaryKeys = arrayOf("itemId", "userId"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId")),
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("itemId"))

    ))

data class UsersItems(val countInCart: Int,
                      val countInBuy: Int)