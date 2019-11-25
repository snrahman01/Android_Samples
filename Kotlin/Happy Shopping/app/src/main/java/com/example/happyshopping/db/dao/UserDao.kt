package com.example.happyshoping.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.happyshoping.db.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table WHERE id LIKE :id")
    fun getUser(id:Int): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}
