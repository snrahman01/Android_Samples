package com.example.medicationassistance.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.medicationassistance.R
import com.example.medicationassistance.databinding.ActivityAddMedicineBinding
import com.example.medicationassistance.databinding.ActivityMainBinding
import com.example.medicationassistance.db.entity.PrescribedMedicine
import com.example.medicationassistance.viewmodel.AddMedicineViewModel
import com.example.medicationassistance.viewmodel.AddMedicineViewModelFactory
import com.example.medicationassistance.viewmodel.MedicineViewModel
import com.example.medicationassistance.viewmodel.MedicineViewModelFactory
import com.google.android.material.snackbar.Snackbar

class AddMedicineActivity : AppCompatActivity() {
    private val viewModel: AddMedicineViewModel by lazy {
        ViewModelProvider(this, AddMedicineViewModelFactory(application))
            .get(AddMedicineViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddMedicineBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_add_medicine
        )

        binding.callback = object : Callback {
            override fun add() {

                var medicine = PrescribedMedicine(binding.txtMediName.text.toString(),
                                                    binding.edittextMediDes.text.toString(),
                                                    binding.edittextMediFreq.text.toString().toInt(),
                                                    binding.switchMeal.isChecked)
                viewModel.insert(medicine)
                Snackbar.make(binding.root, "Added Medicine", Snackbar.LENGTH_LONG)
                    .show()
            }

        }
    }
    interface Callback{
        fun add()
    }
}
