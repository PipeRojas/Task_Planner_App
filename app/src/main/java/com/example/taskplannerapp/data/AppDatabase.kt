package com.example.taskplannerapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskplannerapp.data.dao.TaskDao
import com.example.taskplannerapp.data.entity.Task

@Database(entities = [Task::class], version = 1)
@TypeConverters(CustomConverters::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun taskDao(): TaskDao
}