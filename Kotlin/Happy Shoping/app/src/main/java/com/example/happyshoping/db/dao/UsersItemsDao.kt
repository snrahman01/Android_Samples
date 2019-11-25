package com.example.happyshoping.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.happyshoping.db.entity.UsersItems

@Dao
interface UsersItemsDao {

    @Query("SELECT * FROM item_table")
    fun getAllItemOfUser(userId: Int): LiveData<List<UsersItems>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: UsersItems)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}