package com.example.cleanarchitecture_todo.data.repository

import com.example.cleanarchitecture_todo.data.entity.ToDoEntity

interface ToDoRepository {

    suspend fun getToDoList(): List<ToDoEntity>

    suspend fun getToDoItem(id: Long): ToDoEntity?

    suspend fun insertToDoList(toDoList: List<ToDoEntity>)

    suspend fun updateToDoItem(toDoEntity: ToDoEntity) : Boolean

    suspend fun insertToDoItem(toDoItem: ToDoEntity) : Long

    suspend fun deleteToDoItem(id: Long) : Boolean

    suspend fun deleteAll()

}