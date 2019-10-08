package com.example.medicationassistance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medicationassistance.R
import com.example.medicationassistance.databinding.PrescriptionItemBinding
import com.example.medicationassistance.db.entity.PrescribedMedicine

class MedicineListAdapter:
    ListAdapter<PrescribedMedicine, MedicineListAdapter.MedicineViewHolder>(MedicineDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val itemView = PrescriptionItemBinding.inflate(
                        LayoutInflater.from(parent.context),parent, false)
        return MedicineViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        getItem(position).let { medicine ->
            with(holder) {
                bind(medicine)
            }
        }
    }

    class MedicineViewHolder(private val binding: PrescriptionItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PrescribedMedicine){
            binding.apply {
                medicine = item
                executePendingBindings()
            }
        }
    }

}

private class MedicineDiffCallback : DiffUtil.ItemCallback<PrescribedMedicine>() {

    override fun areItemsTheSame(
        oldItem: PrescribedMedicine,
        newItem: PrescribedMedicine
    ): Boolean {
        return oldItem.mMediName == newItem.mMediName
    }

    override fun areContentsTheSame(
        oldItem: PrescribedMedicine,
        newItem: PrescribedMedicine
    ): Boolean {
        return oldItem == newItem
    }
}