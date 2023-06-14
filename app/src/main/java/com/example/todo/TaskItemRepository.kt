package com.example.todo

import android.content.Context
import android.provider.Settings.Global.getString
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TaskItemRepository(private val taskItemDao: TaskItemDao, context: Context) {

    private val context = context
    @WorkerThread
    public fun loadTaskList(): Flow<List<TaskItem>>{
        var allTaskItems : Flow<List<TaskItem>> = taskItemDao.allTaskItems()
        val sharedPref = context.getSharedPreferences("todo.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val sortType = sharedPref.getString("sortMode","")
        val hideCompleted = sharedPref.getString("hideCompleted","")
        if(sortType == "created"){
            if(hideCompleted == "true"){
                allTaskItems = taskItemDao.incompleteTaskItems()
            }
            else {
                allTaskItems = taskItemDao.allTaskItems()
            }
        }
        else{
            if(hideCompleted == "true"){
                allTaskItems = taskItemDao.incompleteTaskItemsSorted()
            }
            else {
                allTaskItems = taskItemDao.allTaskItemsSorted()
            }
        }
        return allTaskItems
    }

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