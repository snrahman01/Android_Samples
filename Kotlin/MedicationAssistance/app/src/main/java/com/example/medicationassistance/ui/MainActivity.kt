package com.example.medicationassistance.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicationassistance.R
import com.example.medicationassistance.adapter.MedicineListAdapter
import com.example.medicationassistance.databinding.ActivityMainBinding
import com.example.medicationassistance.db.AssistanceDatabase
import com.example.medicationassistance.db.entity.PrescribedMedicine
import com.example.medicationassistance.repository.MedicineRepository
import com.example.medicationassistance.viewmodel.MedicineViewModel
import com.example.medicationassistance.viewmodel.MedicineViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), LifecycleOwner  {

    private val viewModel: MedicineViewModel by lazy {
        ViewModelProvider(this, MedicineViewModelFactory(application))
            .get(MedicineViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        binding.callback = object : Callback{
            override fun addNewMedicine() {

                var intent = Intent(viewModel.getApplication(), AddMedicineActivity::class.java)

                   startActivity(intent) /*viewModel.insert(medicine)
                    Snackbar.make(binding.root, "Added Medicine", Snackbar.LENGTH_LONG)
                        .show()*/
            }

        }

        binding.viewModel = this.viewModel
        val adapter = MedicineListAdapter()
        binding.recyclerviewMedicine.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: MedicineListAdapter) {
        viewModel.allMedicines.observe(this) { medicines ->
            adapter.submitList(medicines)
        }
    }
    interface Callback{
        fun addNewMedicine()
    }
}
