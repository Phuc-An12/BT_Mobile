package com.example.smartuthtasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartuthtasks.network.Task
import com.example.smartuthtasks.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val repository = TaskRepository()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun loadTasks() {
        viewModelScope.launch {
            try {
                _tasks.value = repository.getTasks()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            try {
                val newTask = repository.addTask(Task(title = title))
                if (newTask != null) {
                    _tasks.value = _tasks.value + newTask
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteTask(id)
                _tasks.value = _tasks.value.filterNot { it.id == id }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
