package com.example.todo

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList

class TaskItemRepository(private val taskItemDao: TaskItemDao) {

    var allTaskItems: Flow<List<TaskItem>> = taskItemDao.allTaskItems()


    @WorkerThread
    suspend fun insertTaskItem(taskItem : TaskItem){
        taskItemDao.insertTaskItem(taskItem)
    }

    @WorkerThread
    suspend fun updateTaskItem(taskItem : TaskItem){
        taskItemDao.updateTaskItem(taskItem)
    }

    @WorkerThread
    suspend fun deleteTaskItem(taskItem : TaskItem){
        taskItemDao.deleteTaskItem(taskItem)
    }
}