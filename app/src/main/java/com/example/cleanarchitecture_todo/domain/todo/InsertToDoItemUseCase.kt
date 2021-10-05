package com.example.cleanarchitecture_todo.domain.todo

import com.example.cleanarchitecture_todo.data.entity.ToDoEntity
import com.example.cleanarchitecture_todo.data.repository.ToDoRepository
import com.example.cleanarchitecture_todo.domain.UseCase

internal class InsertToDoItemUseCase(
    private val toDoRepository: ToDoRepository
) :UseCase {

    suspend operator fun invoke(toDoItem: ToDoEntity): Long{
        return toDoRepository.insertToDoItem(toDoItem)
    }
}