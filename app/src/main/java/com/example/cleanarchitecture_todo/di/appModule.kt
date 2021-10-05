package com.example.cleanarchitecture_todo.di

import android.content.Context
import androidx.room.Room
import com.example.cleanarchitecture_todo.data.local.db.ToDoDatabase
import com.example.cleanarchitecture_todo.data.repository.DefaultToDoRepository
import com.example.cleanarchitecture_todo.data.repository.ToDoRepository
import com.example.cleanarchitecture_todo.domain.todo.*
import com.example.cleanarchitecture_todo.domain.todo.DeleteAllToDoItemUseCase
import com.example.cleanarchitecture_todo.domain.todo.DeleteToDoItemUseCase
import com.example.cleanarchitecture_todo.domain.todo.GetToDoItemUseCase
import com.example.cleanarchitecture_todo.domain.todo.GetToDoListUseCase
import com.example.cleanarchitecture_todo.domain.todo.InsertToDoItemUseCase
import com.example.cleanarchitecture_todo.domain.todo.InsertToDoListUseCase
import com.example.cleanarchitecture_todo.domain.todo.UpdateToDoUseCase
import com.example.cleanarchitecture_todo.presentation.detail.DetailMode
import com.example.cleanarchitecture_todo.presentation.detail.DetailViewModel
import com.example.cleanarchitecture_todo.presentation.list.ListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    //ViewModel
    viewModel { ListViewModel(get(),get(),get ()) }
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),
            get(),
            get(),
            get()
        ) }

    //UseCase
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { InsertToDoItemUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }

    //Repository
    single<ToDoRepository> { DefaultToDoRepository(get(), get()) }

    single { provideDB(androidApplication())}
    single { provideToDoDao(get()) }
}

internal fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()

internal fun provideToDoDao(database: ToDoDatabase) = database.toDoDao()
