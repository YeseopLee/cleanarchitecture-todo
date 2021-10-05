package com.example.cleanarchitecture_todo.viewmodel.todo

import com.example.cleanarchitecture_todo.data.entity.ToDoEntity
import com.example.cleanarchitecture_todo.presentation.detail.DetailMode
import com.example.cleanarchitecture_todo.presentation.detail.DetailViewModel
import com.example.cleanarchitecture_todo.presentation.detail.ToDoDetailState
import com.example.cleanarchitecture_todo.presentation.list.ListViewModel
import com.example.cleanarchitecture_todo.presentation.list.ToDoListState
import com.example.cleanarchitecture_todo.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

/**
 * [DetailViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. test viewModel fetch
 * 2. test insert item
 *
 * */

@ExperimentalCoroutinesApi
internal class DetailViewModelForWriteTest: ViewModelTest() {

    private val id = 0L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.WRITE, id) }
    private val listViewModel by inject<ListViewModel>()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Test
    fun `test viewModel fetch`(): Unit = runBlockingTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()
        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Write
            )
        )

    }

    @Test
    fun `test insert item`() : Unit = runBlockingTest {
        val detailTestObservable = detailViewModel.toDoDetailLiveData.test()
        val listTestObservable = listViewModel.toDoListLiveData.test()

        detailViewModel.writeItem(
            title = todo.title,
            description = todo.description
        )

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )

        assert(detailViewModel.detailMode == DetailMode.DETAIL)
        assert(detailViewModel.id == id)

        // 뒤로 나가서 리스트 보기
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf(
                    todo
                ))
            )

        )

    }



}