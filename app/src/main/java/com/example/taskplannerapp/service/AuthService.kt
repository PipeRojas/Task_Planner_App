package com.example.taskplannerapp.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth")
    suspend fun authenticate(@Body credentials: AuthRequest): Response<AuthResponse>

}