package com.example.mykitchen.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mykitchen.data.FoodItem
import com.example.mykitchen.databinding.FoodItemViewBinding

class FoodItemsAdapter(val itemClicked: OnItemClickListener):
ListAdapter<
        FoodItem,
        FoodItemsAdapter.FoodItemViewHolder>(FoodItemDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val itemView = FoodItemViewBinding.inflate(
            LayoutInflater.from(parent.context),parent, false)
        return FoodItemViewHolder(itemView)    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        getItem(position).let { item ->
            with(holder) {
                bind(item, itemClicked)
            }
        }
    }


    class FoodItemViewHolder(
        private val binding: FoodItemViewBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FoodItem, itemClicked: OnItemClickListener){
            binding.apply {
                food = item
                onClick = itemClicked
                executePendingBindings()
            }


        }
    }
}

private class FoodItemDiffCallback : DiffUtil.ItemCallback<FoodItem>() {

    override fun areItemsTheSame(
        oldItem: FoodItem,
        newItem: FoodItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FoodItem,
        newItem: FoodItem
    ): Boolean {
        return oldItem == newItem
    }
}

interface OnItemClickListener{
    fun onItemClicked(food: FoodItem)
}