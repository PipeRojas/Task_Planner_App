package com.example.taskplannerapp.service

import com.example.taskplannerapp.storage.LocalStorage
import okhttp3.Interceptor
import okhttp3.Response

class JWTInterceptor(private val localStorage: LocalStorage):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = localStorage.getToken()
        if(token != null){
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}