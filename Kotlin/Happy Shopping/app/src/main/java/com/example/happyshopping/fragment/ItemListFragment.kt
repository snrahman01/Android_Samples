package com.example.happyshoping.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe

import com.example.happyshoping.R
import com.example.happyshoping.adapter.ItemAdapter
import com.example.happyshoping.databinding.FragmentItemListBinding
import com.example.happyshoping.ui.MainActivity
import com.example.happyshoping.viewmodel.ItemsViewModel
import com.example.happyshoping.viewmodel.ItemsViewModelFactory
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ItemListFragment : Fragment() {


    @Inject
    lateinit var itemsViewModelFactory: ItemsViewModelFactory
    private lateinit var itemViewModel: ItemsViewModel

    private lateinit var binding:FragmentItemListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemListBinding.inflate(
            inflater, container, false
        )

        context ?: return binding.root


        itemViewModel = itemsViewModelFactory.create(ItemsViewModel::class.java)

        binding.itemsRecycler.adapter = ItemAdapter()
        subscribeUi()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)

           }

    override fun onDetach() {
        super.onDetach()
    }

    private fun subscribeUi() {
        itemViewModel.getItems().observe(viewLifecycleOwner) { itemList ->
            var s = itemList[0].mTitle
            (binding.itemsRecycler.adapter as ItemAdapter).submitList(itemList)
        }
    }

}
