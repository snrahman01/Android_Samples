package com.example.happyshoping.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.happyshoping.repository.ItemsRepository
import javax.inject.Inject

class ItemsViewModelFactory
@Inject
constructor(private val repository: ItemsRepository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>)=ItemsViewModel(repository) as T
}