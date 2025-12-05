package com.example.smartuthtasks.network

import retrofit2.http.*

interface ApiService {

    @GET("tasks")
    suspend fun getTasks(): List<Task>

    @POST("tasks")
    suspend fun addTask(@Body task: Task): Task

    @DELETE("task/{id}")
    suspend fun deleteTask(@Path("id") id: Int)
}
