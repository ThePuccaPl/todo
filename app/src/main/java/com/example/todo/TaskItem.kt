package com.example.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity(tableName = "todo")
class TaskItem(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var desc: String,
    @ColumnInfo(name = "dueDateTime") var dueDateTime: String?,
    @ColumnInfo(name = "completedDate") var completedDate: String?,
    @ColumnInfo(name = "category") var category: String?,
    @ColumnInfo(name = "file") var file: String?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "createdDate") var creationDate: String = LocalDate.now().toString()
) : Serializable
{

    fun completedDate(): LocalDate? = if (completedDate == null) null else LocalDate.parse(completedDate, dateFormatter)

    fun createdDate(): LocalDate? = LocalDate.parse(creationDate, dateFormatter)

    fun dueDateTime(): LocalDateTime? = if (dueDateTime == null) null else LocalDateTime.parse(dueDateTime, dateTimeFormatter)

    fun isCompleted() = completedDate != null
    fun imageResource(): Int = if(isCompleted()) R.drawable.baseline_check_circle_outline_24 else R.drawable.baseline_unchecked_24

    companion object{
        val dateTimeFormatter : DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        val dateFormatter : DateTimeFormatter = DateTimeFormatter.ISO_DATE
    }

}