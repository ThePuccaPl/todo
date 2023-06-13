package com.example.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.databinding.FragmentDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.format.DateTimeFormatter

class DetailsTaskFragment(val taskItem: TaskItem): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity()
        binding.taskNameDetails.text = taskItem.name
        binding.taskDescDetails.text = taskItem.desc
        if(taskItem.dueDateTime!=null){
            binding.taskTimeDetails.text = timeFormat.format(taskItem.dueDateTime())
        }
        binding.taskCategoryDetails.text = taskItem.category

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
}