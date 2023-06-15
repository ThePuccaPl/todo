package com.example.todo

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.databinding.FragmentCategoryInputBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CategoryInputFragment(private var viewModel: TaskViewModel) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCategoryInputBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity()
        val editable = Editable.Factory.getInstance()
        binding.categoryInput.text = editable.newEditable(viewModel.category)
        binding.cancelCategorySearchButton.setOnClickListener {
            dismiss()
        }
        binding.saveCategorySearchButton.setOnClickListener {
            val searchedCategory = binding.categoryInput.text.toString()
            viewModel.category = searchedCategory
            viewModel.reloadItems()
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryInputBinding.inflate(inflater, container, false)
        return binding.root
    }

}