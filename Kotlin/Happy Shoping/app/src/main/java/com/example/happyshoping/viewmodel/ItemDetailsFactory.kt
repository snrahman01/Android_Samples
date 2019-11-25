package com.example.happyshoping.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.happyshoping.repository.ItemsRepository
import javax.inject.Inject

class ItemDetailsFactory @Inject
    constructor(
    private val itemRepository:ItemsRepository
) :ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?>
            create(modelClass: Class<T>)=
                ItemsViewModel(itemRepository) as T

}