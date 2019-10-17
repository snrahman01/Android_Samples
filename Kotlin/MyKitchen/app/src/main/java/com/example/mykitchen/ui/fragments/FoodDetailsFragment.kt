package com.example.mykitchen.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.example.mykitchen.R
import com.example.mykitchen.databinding.FragmentFoodDetailsBinding
import com.example.mykitchen.viewmodel.FoodDetailsViewModel
import com.example.mykitchen.viewmodel.FoodDetailsViewModelFactory
import com.example.mykitchen.viewmodel.FoodItemViewModel
import com.example.mykitchen.viewmodel.FoodItemViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class FoodDetailsFragment : Fragment() {


    private lateinit var binding: FragmentFoodDetailsBinding
    private val viewModel: FoodDetailsViewModel by lazy {
        ViewModelProvider(this, FoodDetailsViewModelFactory(
            arguments!!.getInt("id")))
            .get(FoodDetailsViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        binding.food = viewModel.food

        return binding.root
    }


}
