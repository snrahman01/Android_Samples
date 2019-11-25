package com.example.happyshoping.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyshoping.MyApplication
import com.example.happyshoping.R
import com.example.happyshoping.di.AppComponent
import com.example.happyshoping.viewmodel.ItemsViewModel
import com.example.happyshoping.viewmodel.ItemsViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    lateinit var appComponent: AppComponent


    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = (application as MyApplication).
            appComponent
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
