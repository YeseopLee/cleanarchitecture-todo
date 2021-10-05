package com.example.cleanarchitecture_todo.viewmodel.todo

import com.example.cleanarchitecture_todo.data.entity.ToDoEntity
import com.example.cleanarchitecture_todo.domain.todo.InsertToDoItemUseCase
import com.example.cleanarchitecture_todo.presentation.detail.DetailMode
import com.example.cleanarchitecture_todo.presentation.detail.DetailViewModel
import com.example.cleanarchitecture_todo.presentation.detail.ToDoDetailState
import com.example.cleanarchitecture_todo.presentation.list.ListViewModel
import com.example.cleanarchitecture_todo.presentation.list.ToDoListState
import com.example.cleanarchitecture_todo.viewmodel.ViewModelTest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import java.lang.Exception

/**
 * [DetailViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test delete item
 * 4. test update item
 *
 * */


internal class DetailViewModelTest: ViewModelTest() {

    private val id = 1L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.DETAIL, id) }
    private val listViewModel by inject<ListViewModel>()
    private val insertToDoItemUseCase: InsertToDoItemUseCase by inject()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runBlockingTest {
        insertToDoItemUseCase(todo)
    }

    @Test
    fun `test viewModel fetch`(): Unit = runBlockingTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()
        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )

    }

    @Test
    fun `test delete item`(): Unit = runBlockingTest {
        val detailTestObservable = detailViewModel.toDoDetailLiveData.test()
        detailViewModel.deleteItem()

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Delete
            )
        )

        val listTestObservable = listViewModel.toDoListLiveData.test()

        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )

    }

    @Test
    fun `test update todo`() : Unit = runBlockingTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()

        val updateTitle = "title 1 updated"
        val updateDesc = "desc 1 updated"

        val updateToDo = todo.copy(
            title = updateTitle,
            description = updateDesc
        )

        detailViewModel.writeItem(
            title = updateTitle,
            description = updateDesc
        )

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(updateToDo)
            )
        )
    }


}