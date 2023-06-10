package com.example.todo

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.TaskItemBinding
import java.io.File
import java.time.format.DateTimeFormatter


class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemBinding,
    private val clickListener: TaskItemClickListener
): RecyclerView.ViewHolder(binding.root)
{
    private val timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    fun bindTaskItem(taskItem: TaskItem) {

        binding.taskName.text = taskItem.name
        binding.taskDesctiption.text = taskItem.desc
        binding.category.text = taskItem.category
        if(taskItem.file!=null){
            binding.fileButton.visibility = android.view.View.VISIBLE
        }
        else{
            binding.fileButton.visibility = android.view.View.GONE
        }
        if (taskItem.dueDateTime != null) {
            //if(timeFormat.format(taskItem.dueDateTime()) == "1900-01-01T00:00:00")
            binding.dueTime.text = timeFormat.format(taskItem.dueDateTime())
        } else {
            binding.dueTime.text = ""
        }

        if (taskItem.isCompleted()) {
            binding.taskName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.completeButton.setImageResource(taskItem.imageResource())

        binding.completeButton.setOnClickListener {
            clickListener.completeTaskItem(taskItem)
        }
        binding.taskLayout.setOnClickListener {
            //display details fragment
        }
        binding.editButton.setOnClickListener {
            clickListener.editTaskItem(taskItem)
        }
        binding.deleteButton.setOnClickListener {
            clickListener.deleteTaskItem(taskItem)
        }
        binding.fileButton.setOnClickListener {
            println(taskItem.file)
            var fileString = taskItem.file?.substring(7)
            var file = File(fileString)

            val uri = FileProvider.getUriForFile(context!!, "com.example.todo.provider", file)
            val mime: String = context!!.getContentResolver().getType(uri)!!

            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, mime)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context!!.startActivity(intent)
        }
    }
}