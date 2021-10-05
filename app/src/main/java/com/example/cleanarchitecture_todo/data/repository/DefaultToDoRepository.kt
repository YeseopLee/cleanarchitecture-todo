package com.example.cleanarchitecture_todo.data.repository

import com.example.cleanarchitecture_todo.data.entity.ToDoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultToDoRepository(

): ToDoRepository {
    override suspend fun getToDoList(): List<ToDoEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getToDoItem(id: Long): ToDoEntity? {
        TODO("Not yet implemented")
    }


    override suspend fun insertToDoList(toDoList: List<ToDoEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateToDoItem(toDoEntity: ToDoEntity) : Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun insertToDoItem(toDoItem: ToDoEntity) : Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteToDoItem(id: Long): Boolean {
        TODO("Not yet implemented")
    }


    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }


}
