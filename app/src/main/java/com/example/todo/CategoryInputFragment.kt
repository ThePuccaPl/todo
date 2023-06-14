package com.example.todo

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.databinding.FragmentCategoryInputBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CategoryInputFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCategoryInputBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity()
        val sharedPref = requireContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val savedCat = sharedPref.getString("categories","")
        val editable = Editable.Factory.getInstance()
        binding.categoryInput.text = editable.newEditable(savedCat)
        binding.cancelCategorySearchButton.setOnClickListener {
            dismiss()
        }
        binding.saveCategorySearchButton.setOnClickListener {
            val editor = sharedPref.edit()
            val searchedCategory = binding.categoryInput.text.toString()
            editor.putString("categories",searchedCategory)
            editor.apply()
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