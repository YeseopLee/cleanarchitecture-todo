package com.example.cleanarchitecture_todo.viewmodel.todo

import com.example.cleanarchitecture_todo.data.entity.ToDoEntity
import com.example.cleanarchitecture_todo.domain.todo.GetToDoItemUseCase
import com.example.cleanarchitecture_todo.domain.todo.InsertToDoListUseCase
import com.example.cleanarchitecture_todo.presentation.list.ListViewModel
import com.example.cleanarchitecture_todo.presentation.list.ToDoListState
import com.example.cleanarchitecture_todo.viewmodel.ViewModelTest
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.test.inject


/**
 * [ListViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test item update
 * 4. test item delete all
 *
 * */

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
internal class ListViewModelTest : ViewModelTest() {


    private val viewModel: ListViewModel by inject()

    private val insertToDoListUseCase: InsertToDoListUseCase by inject()
    private val getToDoItemUseCase: GetToDoItemUseCase by inject()

    private val list = (0 until 10).map {
        ToDoEntity(
            id = it.toLong(),
            title = "title $it",
            description = "description $it",
            hasCompleted = false
        )
    }

    /**
     * 필요한 UseCase
     * 1. InsertToDoListUseCase
     * 2. GetToDoItemUseCase
     *
     * */

    @Before
    fun init() {
        initData()
    }

    private fun initData() = runBlockingTest {
        insertToDoListUseCase(list)
    }

    // 입력된 데이터를 불러와서 검증한다.

    @Test
    fun `test viewModel fetch`(): Unit = runBlockingTest {
        val testObservable = viewModel.toDoListLiveData.test()

        viewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(list)
            )
        )

    }

    // 데이터 업데이트를 검증한다.

    @Test
    fun `test item update`(): Unit = runBlockingTest {
        val todo = ToDoEntity(
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        viewModel.updateEntity(todo)
        assert(getToDoItemUseCase(todo.id)?.hasCompleted ?: false == todo.hasCompleted)
    }

    // 데이터 삭제를 검증한다.

    @Test
    fun `test item delete all`(): Unit = runBlockingTest {
        val testObservable = viewModel.toDoListLiveData.test()
        viewModel.deleteAll()
        testObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }
}