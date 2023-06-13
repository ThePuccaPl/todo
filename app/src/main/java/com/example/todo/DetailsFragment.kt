package com.example.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.time.format.DateTimeFormatter


class DetailsFragment(taskItem: TaskItem) : Fragment(R.layout.fragment_details2) {

    private val timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    var t = taskItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details2, container, false)
        val textViewName: TextView = view.findViewById(R.id.taskNameDetails)
        val textViewDesc: TextView = view.findViewById(R.id.taskDescDetails)
        val textViewCategory: TextView = view.findViewById(R.id.taskCategoryDetails)
        val textViewTime: TextView = view.findViewById(R.id.taskTimeDetails)
        textViewName.text = t.name
        textViewDesc.text = t.desc
        textViewCategory.text = t.category
        if(t.dueDateTime!=null){
            textViewTime.text = timeFormat.format(t.dueDateTime())
        }
        return view
    }

}