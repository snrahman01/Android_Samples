package com.example.happyshoping.di

import android.app.Application
import android.content.Context
import com.example.happyshoping.fragment.ItemListFragment
import com.example.happyshoping.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DaoModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun inject(fragment: ItemListFragment)
}