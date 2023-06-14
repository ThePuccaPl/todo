package com.example.todo

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TaskItemRepository(private val taskItemDao: TaskItemDao, context: Context) {

    private val context = context

    var allTaskItems : Flow<List<TaskItem>> = taskItemDao.allTaskItems()

    @WorkerThread
    fun loadTaskList(): Flow<List<TaskItem>>{
        val sharedPref = context.getSharedPreferences("todo.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val sortType = sharedPref.getString("sortMode","")
        val hideCompleted = sharedPref.getString("hideCompleted","")
        val category = sharedPref.getString("categories","").toString()
        if(sortType == "created"){
            if(category == ""){
                if(hideCompleted == "true"){
                    allTaskItems = taskItemDao.incompleteTaskItems()
                }
                else {
                    allTaskItems =  taskItemDao.allTaskItems()
                }
            }
            else{
                if(hideCompleted == "true"){
                    allTaskItems = taskItemDao.incompleteTaskItemsCategory(category)
                }
                else {
                    allTaskItems = taskItemDao.allTaskItemsCategory(category)
                }
            }
        }
        else{
            if(category == ""){
                if(hideCompleted == "true"){
                    allTaskItems = taskItemDao.incompleteTaskItemsSorted()
                }
                else {
                    allTaskItems = taskItemDao.allTaskItemsSorted()
                }
            }
            else{
                if(hideCompleted == "true"){
                    allTaskItems = taskItemDao.incompleteTaskItemsCategorySorted(category)
                }
                else {
                    allTaskItems = taskItemDao.allTaskItemsCategorySorted(category)
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