package com.example.todo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.R.string.preference_file_key
import com.example.todo.databinding.FragmentSetTimeOffsetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetTimeOffsetFragment : BottomSheetDialogFragment(){
    private lateinit var binding: FragmentSetTimeOffsetBinding
    private var number = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity()
        val sharedPref = requireContext().getSharedPreferences(getString(preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        number = sharedPref.getInt("notifTimeOffset",0)
        binding.numberPicker1.maxValue = 60
        binding.numberPicker1.minValue = 0
        binding.numberPicker1.wrapSelectorWheel = false
        binding.numberPicker1.setOnValueChangedListener { _, _, newVal ->
            number = newVal
        }
        binding.cancelOffsetButton.setOnClickListener {
            dismiss()
        }
        binding.saveOffsetButton.setOnClickListener {
            editor.putInt("notifTimeOffset",number)
            editor.apply()
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSetTimeOffsetBinding.inflate(inflater,container,false)
        return binding.root
    }

}