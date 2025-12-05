package com.example.smartuthtasks.repository

import com.example.smartuthtasks.network.ApiService
import com.example.smartuthtasks.network.Task
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TaskRepository {

    private val api: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://amock.io/api/researchUTH/tasks1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)
    }

    suspend fun getTasks(): List<Task> {
        return try {
            api.getTasks()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addTask(task: Task): Task? {
        return try {
            api.addTask(task)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteTask(id: Int) {
        try {
            api.deleteTask(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
