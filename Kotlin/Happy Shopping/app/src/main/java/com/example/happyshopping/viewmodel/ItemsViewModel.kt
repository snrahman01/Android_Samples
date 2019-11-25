package com.example.happyshoping.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.happyshoping.db.entity.Item
import com.example.happyshoping.repository.ItemsRepository
import javax.inject.Inject

class ItemsViewModel
    @Inject constructor(
        repository: ItemsRepository
    ):ViewModel(){

    private val itemList = repository.allItems

    fun getItems(): LiveData<List<Item>>{
        return itemList
    }
}