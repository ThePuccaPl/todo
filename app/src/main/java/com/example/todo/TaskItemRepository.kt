package com.example.todo

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TaskItemRepository(private val taskItemDao: TaskItemDao, context: Context) {

    private val context = context
    @WorkerThread
    fun loadTaskList(): Flow<List<TaskItem>>{
        var allTaskItems : Flow<List<TaskItem>>
        val sharedPref = context.getSharedPreferences("todo.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val sortType = sharedPref.getString("sortMode","")
        val hideCompleted = sharedPref.getString("hideCompleted","")
        val category = sharedPref.getString("categories","").toString()
        if(sortType == "created"){
            allTaskItems = if(category == ""){
                if(hideCompleted == "true"){
                    taskItemDao.incompleteTaskItems()
                } else {
                    taskItemDao.allTaskItems()
                }
            } else{
                if(hideCompleted == "true"){
                    taskItemDao.incompleteTaskItemsCategory(category)
                } else {
                    taskItemDao.allTaskItemsCategory(category)
                }
            }
        }
        else{
            allTaskItems = if(category == ""){
                if(hideCompleted == "true"){
                    taskItemDao.incompleteTaskItemsSorted()
                } else {
                    taskItemDao.allTaskItemsSorted()
                }
            } else{
                if(hideCompleted == "true"){
                    taskItemDao.incompleteTaskItemsCategorySorted(category)
                } else {
                    taskItemDao.allTaskItemsCategorySorted(category)
                }
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