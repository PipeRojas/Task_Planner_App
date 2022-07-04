package com.example.taskplannerapp.service

import java.util.*

data class TaskDTO (var id:String, var name:String, var description:String, var status:String, var assignedTo:String, var dueDate:Date)