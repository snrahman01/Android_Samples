package com.example.happyshoping

import android.app.Application
import com.example.happyshoping.di.AppComponent
import com.example.happyshoping.di.DaggerAppComponent

open class MyApplication: Application() {

    val appComponent:AppComponent by lazy{
        DaggerAppComponent.factory().create(applicationContext)
    }
}