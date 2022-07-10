package com.example.taskplannerapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.taskplannerapp.data.entity.Task

@Dao
interface TaskDao {
    @Query("select * from task")
    fun getAll(): List<Task>

    @Query("select * from task where id in (:taskIds)")
    fun loadAllByIds(taskIds: List<String>): List<Task>

    @Query("select * from task where name like :name limit 1")
    fun findByName(name: String): Task

    @Insert
    fun insert(vararg task: Task)

    @Insert
    fun insertAll(tasks: List<Task>)

    @Delete
    fun delete(task: Task)
}