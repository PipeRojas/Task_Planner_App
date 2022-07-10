package com.example.taskplannerapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskplannerapp.service.TaskDTO
import java.util.*

@Entity
data class Task(@PrimaryKey var id:String, @ColumnInfo(name = "name") var name:String, @ColumnInfo(name = "description") var description:String, @ColumnInfo(name = "status") var status:String, @ColumnInfo(name = "assignedTo") var assignedTo:String, @ColumnInfo(name = "dueDate") var dueDate: Date){
    constructor(taskDTO: TaskDTO) : this(taskDTO.id, taskDTO.name, taskDTO.description, taskDTO.status, taskDTO.assignedTo, taskDTO.dueDate)
}