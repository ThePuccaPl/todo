package com.example.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskItemDao {

    @Query("SELECT * FROM todo ORDER BY createdDate ASC")
    fun allTaskItems(): Flow<List<TaskItem>>

    @Query("SELECT * FROM todo WHERE name LIKE '%' || :searchQuery || '%'")
    fun searchTaskItems(searchQuery: String): Flow<List<TaskItem>>

    @Query("SELECT * FROM (SELECT * FROM todo WHERE dueDateTime IS NOT NULL ORDER BY dueDateTime ASC)\n" +
            "UNION ALL\n" +
            "SELECT * from (SELECT * FROM todo WHERE dueDateTime IS NULL ORDER BY dueDateTime ASC)\n")
    fun allTaskItemsSorted(): Flow<List<TaskItem>>

    @Query("SELECT * FROM (SELECT * FROM todo WHERE dueDateTime IS NOT NULL ORDER BY dueDateTime ASC) WHERE completedDate IS NULL\n" +
            "UNION ALL\n" +
            "SELECT * from (SELECT * FROM todo WHERE dueDateTime IS NULL ORDER BY dueDateTime ASC) WHERE completedDate IS NULL\n")
    fun incompleteTaskItemsSorted(): Flow<List<TaskItem>>

    @Query("SELECT * FROM todo WHERE completedDate IS NULL ORDER BY createdDate ASC")
    fun incompleteTaskItems(): Flow<List<TaskItem>>

    @Query("SELECT * FROM todo WHERE category = :searchedCategory ORDER BY createdDate ASC")
    fun allTaskItemsCategory(searchedCategory :String): Flow<List<TaskItem>>

    @Query("SELECT * FROM (SELECT * FROM todo WHERE dueDateTime IS NOT NULL ORDER BY dueDateTime ASC) WHERE category = :searchedCategory \n" +
            "UNION ALL\n" +
            "SELECT * from (SELECT * FROM todo WHERE dueDateTime IS NULL ORDER BY dueDateTime ASC) WHERE category = :searchedCategory\n")
    fun allTaskItemsCategorySorted(searchedCategory :String): Flow<List<TaskItem>>

    @Query("SELECT * FROM (SELECT * FROM todo WHERE dueDateTime IS NOT NULL ORDER BY dueDateTime ASC) WHERE completedDate IS NULL AND category = :searchedCategory \n" +
            "UNION ALL\n" +
            "SELECT * from (SELECT * FROM todo WHERE dueDateTime IS NULL ORDER BY dueDateTime ASC) WHERE completedDate IS NULL AND category = :searchedCategory\n")
    fun incompleteTaskItemsCategorySorted(searchedCategory :String): Flow<List<TaskItem>>

    @Query("SELECT * FROM todo WHERE completedDate IS NULL AND category = :searchedCategory ORDER BY createdDate ASC")
    fun incompleteTaskItemsCategory(searchedCategory :String): Flow<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem)

    @Update
    suspend fun updateTaskItem(taskItem: TaskItem)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)
}