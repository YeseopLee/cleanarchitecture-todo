package com.example.cleanarchitecture_todo.domain.todo

import com.example.cleanarchitecture_todo.data.repository.ToDoRepository
import com.example.cleanarchitecture_todo.domain.UseCase

internal class DeleteAllToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke() {
        return toDoRepository.deleteAll()
    }

}
