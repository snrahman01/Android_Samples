package com.example.happyshoping.repository

import androidx.lifecycle.LiveData
import com.example.happyshoping.db.dao.ItemDao
import com.example.happyshoping.db.entity.Item
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepository

    @Inject
    constructor(private val itemDao: ItemDao){

    var allItems: LiveData<List<Item>> = itemDao.getAllItem()

    suspend fun insert(item: Item){
        itemDao.insert(item)
    }
}