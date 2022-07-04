package com.example.taskplannerapp.service

import retrofit2.Response
import retrofit2.http.GET

interface TaskService {

    @GET("api/task/all")
    suspend fun getAllTasks():Response<List<TaskDTO>>
}