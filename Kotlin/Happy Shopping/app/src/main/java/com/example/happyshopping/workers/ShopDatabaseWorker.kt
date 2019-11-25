package com.example.happyshoping.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.happyshoping.db.HappyShopingDatabase
import com.example.happyshoping.db.entity.Item
import com.example.happyshoping.utils.ITEM_DATA_FILENAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.coroutineScope

class ShopDatabaseWorker (context: Context,
workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters){
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(ITEM_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val plantType = object : TypeToken<List<Item>>() {}.type
                    val itemList: List<Item> = Gson().fromJson(jsonReader, plantType)

                    val database = HappyShopingDatabase.getDatabase(applicationContext)
                    database.itemDao().insertAll(itemList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private val TAG = ShopDatabaseWorker::class.java.simpleName
    }
}