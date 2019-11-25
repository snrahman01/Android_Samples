package com.example.happyshoping.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.happyshoping.R
import com.example.happyshoping.databinding.FragmentItemListBinding
import com.example.happyshoping.databinding.ItemViewBinding
import com.example.happyshoping.db.entity.Item
import javax.inject.Inject

class ItemAdapter: ListAdapter<
        Item,
        RecyclerView.ViewHolder>(ItemDiffCallback()){

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {

        val itemView = ItemViewBinding
            .inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let {item ->
            with(holder as ItemViewHolder){
                bind(item)
            }
        }
    }

    private class ItemViewHolder(
        private val binding: ItemViewBinding
    ): RecyclerView.ViewHolder(binding.root){
        init {
            binding.setClickListener {
                binding.item?.let { item ->
                    navigateToWords(item, it)
                }
            }
        }

        private fun navigateToWords(
            item: Item,
            it: View
        ) {

           // it.findNavController().navigate(R.id.action_item_list_to_details)
        }

        fun bind(bindItem: Item){
            binding.apply {
                item = bindItem
                executePendingBindings()
            }
        }

    }
}

private class ItemDiffCallback: DiffUtil.ItemCallback<Item>(){
    override fun areItemsTheSame(
        oldItem: Item,
        newItem: Item
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Item,
        newItem: Item
    ): Boolean {
        return oldItem == newItem    }

}