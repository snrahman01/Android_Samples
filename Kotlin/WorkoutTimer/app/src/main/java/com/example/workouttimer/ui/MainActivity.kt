package com.example.workouttimer.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ObservableInt
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import com.example.workouttimer.R
import com.example.workouttimer.data.IntervalTimerViewModel
import com.example.workouttimer.data.IntervalTimerViewModelFactory
import com.example.workouttimer.databinding.ActivityMainBinding


const val SHARED_PREFS_KEY = "timer"

class MainActivity : AppCompatActivity() {
    private val intervalTimerViewModel: IntervalTimerViewModel by
    lazy { ViewModelProvider(this, IntervalTimerViewModelFactory)
        .get(IntervalTimerViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
        val viewmodel = intervalTimerViewModel
        binding.viewmodel = intervalTimerViewModel

        observeAndSaveTimePerSet(viewmodel.timePerWorkSet, R.string.prefs_timePerWorkSet)
        observeAndSaveTimePerSet(viewmodel.timePerRestSet, R.string.prefs_timePerRestSet)

        observeAndSaveNumberOfSets(viewmodel)

        if (savedInstanceState == null) {
            restorePreferences(viewmodel)
            observeAndSaveNumberOfSets(viewmodel)
        }
    }

    private fun observeAndSaveTimePerSet(timePerWorkSet: ObservableInt, prefsKey: Int){
        timePerWorkSet.addOnPropertyChangedCallback(
            object: Observable.OnPropertyChangedCallback(){

                @SuppressLint("CommitPrefEdits")
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    val sharedPref =  getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) ?: return;
                    sharedPref.edit().apply{
                        putInt(getString(prefsKey), (sender as ObservableInt).get())
                        commit()
                    }
                }

            })
    }

    private fun restorePreferences(viewModel: IntervalTimerViewModel) {
        val sharedPref = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) ?: return
        val timePerWorkSetKey = getString(R.string.prefs_timePerWorkSet)
        var wasAnythingRestored = false

        if (sharedPref.contains(timePerWorkSetKey)) {
            viewModel.timePerWorkSet.set(sharedPref.getInt(timePerWorkSetKey, 100))
            wasAnythingRestored = true
        }
        val timePerRestSetKey = getString(R.string.prefs_timePerRestSet)
        if (sharedPref.contains(timePerRestSetKey)) {
            viewModel.timePerRestSet.set(sharedPref.getInt(timePerRestSetKey, 50))
            wasAnythingRestored = true
        }
        val numberOfSetsKey = getString(R.string.prefs_numberOfSets)
        if (sharedPref.contains(numberOfSetsKey)) {
            viewModel.numberOfSets = arrayOf(0, sharedPref.getInt(numberOfSetsKey, 5))
            wasAnythingRestored = true
        }
        if (wasAnythingRestored) Log.d("saveTimePerWorkSet", "Preferences restored")
        viewModel.stopButtonClicked()
    }

    private fun observeAndSaveNumberOfSets(viewModel: IntervalTimerViewModel) {
        viewModel.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            @SuppressLint("CommitPrefEdits")
            override fun onPropertyChanged(observable: Observable?, p1: Int) {
                if (p1 == BR.numberOfSets) {
                    Log.d("saveTimePerWorkSet", "Saving number of sets preference")
                    val sharedPref =
                        getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE) ?: return
                    sharedPref.edit().apply {
                        putInt(getString(R.string.prefs_numberOfSets), viewModel.numberOfSets[1])
                        commit()
                    }
                }
            }
        })
    }
}
