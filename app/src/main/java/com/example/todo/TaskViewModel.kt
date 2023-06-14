package com.example.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(private val repository: TaskItemRepository): ViewModel()
{
    var taskItems: LiveData<List<TaskItem>> = repository.loadTaskList().asLiveData()

    var hideCompleted = false


    fun addTaskItem(newTask: TaskItem) = viewModelScope.launch {
        repository.insertTaskItem(newTask)
    }

    fun reloadItems() = viewModelScope.launch {
        taskItems = repository.loadTaskList().asLiveData()
    }

    fun updateTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.updateTaskItem(taskItem)
    }

    fun deleteTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.deleteTaskItem(taskItem)
    }

    fun setCompleted(taskItem: TaskItem) = viewModelScope.launch {
        if(!taskItem.isCompleted()){
            taskItem.completedDate = TaskItem.dateFormatter.format(LocalDate.now())
            repository.updateTaskItem(taskItem)
        }
        else{
            taskItem.completedDate = null
            repository.updateTaskItem(taskItem)
            repository.loadTaskList()
            taskItems = repository.loadTaskList().asLiveData()
        }
    }
}

class TaskItemModelFactory(private val repository: TaskItemRepository):ViewModelProvider.Factory{

    override fun <T: ViewModel> create(modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(TaskViewModel::class.java))
            return TaskViewModel(repository) as T
        throw IllegalArgumentException("Unknown class")
    }
}