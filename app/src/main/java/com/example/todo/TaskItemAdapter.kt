package com.example.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.TaskItemBinding

class TaskItemAdapter(
    private var taskItems: List<TaskItem>,
    private val clickListener: TaskItemClickListener,
    private val context: Context
): RecyclerView.Adapter<TaskItemViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(from, parent, false)
        return TaskItemViewHolder(parent.context, binding, clickListener)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val sharedPref = context.getSharedPreferences("todo.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)

        val hideCompleted = sharedPref.getString("hideCompleted","")

        if(hideCompleted == "true"){
            holder.itemView.visibility = View.GONE
        }
        else{
            holder.itemView.visibility = View.VISIBLE
        }


        val sortMode = sharedPref.getString("sortMode","")
        if(sortMode == "created"){
            taskItems = taskItems.sortedBy { it.creationDate }
        }
        else if(sortMode == "time due"){
            taskItems = taskItems.sortedBy { it.dueDateTime }
        }
        holder.bindTaskItem(taskItems[position])
    }

    override fun getItemCount(): Int = taskItems.size


}