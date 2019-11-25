package com.example.happyshoping.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.happyshoping.MyApplication
import com.example.happyshoping.db.dao.ItemDao
import com.example.happyshoping.db.dao.UserDao
import com.example.happyshoping.db.entity.Item
import com.example.happyshoping.db.entity.User
import com.example.happyshoping.workers.ShopDatabaseWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@Database(entities = [Item::class, User::class], version = 1)
abstract class  HappyShopingDatabase:RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun userDao(): UserDao

    private class HappyShopingDatabaseCallback(
        private val context: Context
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val request = OneTimeWorkRequestBuilder<ShopDatabaseWorker>().build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    companion object{
        @Volatile
        private var INSTANCE:HappyShopingDatabase?=null

        fun getDatabase(
            context: Context):HappyShopingDatabase{

            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HappyShopingDatabase::class.java,
                    "happy_shopping_db"
                ).addCallback(
                    (HappyShopingDatabaseCallback(
                        context.applicationContext))).
                    build()
                    INSTANCE = instance
                instance
            }
        }
    }
}