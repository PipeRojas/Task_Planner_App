package com.example.taskplannerapp.storage

interface LocalStorage {

    fun saveToken(token: String)

    fun getToken(): String?

    fun clear()
}