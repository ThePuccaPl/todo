package com.example.todo

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class TaskItemRepository(private val taskItemDao: TaskItemDao, context: Context) {

    private val context = context

    var allTaskItems : Flow<List<TaskItem>> = taskItemDao.allTaskItems()

    @WorkerThread
    fun searchDatabase(searchQuery: String): Flow<List<TaskItem>>{
        allTaskItems =  taskItemDao.searchTaskItems(searchQuery)
        return allTaskItems
    }


    @WorkerThread
    fun loadTaskList(): Flow<List<TaskItem>>{
        allTaskItems =  taskItemDao.allTaskItems()
        return allTaskItems
    }

    @WorkerThread
    fun loadIncompleteTaskList(): Flow<List<TaskItem>>{
        allTaskItems =  taskItemDao.incompleteTaskItems()
        return allTaskItems
    }

    @WorkerThread
    fun loadTaskListSorted(): Flow<List<TaskItem>>{
        allTaskItems =  taskItemDao.allTaskItemsSorted()
        return allTaskItems
    }

    @WorkerThread
    fun loadIncompleteTaskListSorted(): Flow<List<TaskItem>>{
        allTaskItems =  taskItemDao.incompleteTaskItemsSorted()
        return allTaskItems
    }

    @WorkerThread
    fun loadTaskListCategory(category:String): Flow<List<TaskItem>>{
        allTaskItems =  taskItemDao.allTaskItemsCategory(category)
        return allTaskItems
    }

    @WorkerThread
    fun loadTaskListCategorySorted(category:String): Flow<List<TaskItem>>{
        allTaskItems =  taskItemDao.allTaskItemsCategorySorted(category)
        return allTaskItems
    }

    @WorkerThread
    fun loadIncompleteTaskListCategory(category:String): Flow<List<TaskItem>>{
        allTaskItems =  taskItemDao.incompleteTaskItemsCategory(category)
        return allTaskItems
    }

    @WorkerThread
    fun loadIncompleteTaskListCategorySorted(category:String): Flow<List<TaskItem>>{
        allTaskItems =  taskItemDao.incompleteTaskItemsCategorySorted(category)
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