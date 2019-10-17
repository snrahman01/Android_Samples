package com.example.mykitchen.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.mykitchen.R
import com.example.mykitchen.adapters.FoodItemsAdapter
import com.example.mykitchen.adapters.OnItemClickListener
import com.example.mykitchen.data.FoodItem
import com.example.mykitchen.databinding.FragmentFoodItemsBinding
import com.example.mykitchen.viewmodel.FoodItemViewModel
import com.example.mykitchen.viewmodel.FoodItemViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class FoodItemsFragment : Fragment(), OnItemClickListener {
    override fun onItemClicked(food: FoodItem) {
        var result = Bundle()
        result.putInt("id", food.id-1)
        findNavController().navigate(R.id.action_foodlist_to_foodDetails, result)
    }

    private lateinit var binding: FragmentFoodItemsBinding
    private val viewModel: FoodItemViewModel by lazy {
        ViewModelProvider(this, FoodItemViewModelFactory())
            .get(FoodItemViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodItemsBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = FoodItemsAdapter(this)
        binding.foodList.adapter = adapter

        subscribeUi(adapter)
        return binding.root
    }

    private fun subscribeUi(adapter: FoodItemsAdapter) {
            adapter.submitList(viewModel.foodItems)
    }
}
