package com.example.cleanarchitecture_todo.presentation.list

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cleanarchitecture_todo.R
import com.example.cleanarchitecture_todo.databinding.ActivityListBinding
import com.example.cleanarchitecture_todo.presentation.BaseActivity
import com.example.cleanarchitecture_todo.presentation.detail.DetailMode
import com.example.cleanarchitecture_todo.presentation.view.ToDoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

internal class ListActivity : BaseActivity<ListViewModel>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val adapter = ToDoAdapter()

    private lateinit var binding: ActivityListBinding

    override val viewModel: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViews(binding: ActivityListBinding) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@ListActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }

        addToDoButton.setOnClickListener {
//            startActivityForResult(
//                DetailActivity.getIntent(this@ListActivity, DetailMode.WRITE),
//                DetailActivity.FETCH_REQUEST_CODE
//            )
        }
    }

    override fun observeData() {
        viewModel.toDoListLiveData.observe(this) {
            when (it) {
                is ToDoListState.UnInitialized -> {
                    initViews(binding)
                }
                is ToDoListState.Loading -> {
                    handleLoadingState()
                }
                is ToDoListState.Success -> {
                    handleSuccessState(it)
                }
                is ToDoListState.Error -> {
                    handleErrorState()
                }
            }
        }
    }

    private fun handleErrorState() {
        Toast.makeText(this, "에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
    }


    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ToDoListState.Success) = with(binding) {
        refreshLayout.isEnabled = state.toDoList.isNotEmpty()
        refreshLayout.isRefreshing = false

        if (state.toDoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setToDoList(
                state.toDoList,
                toDoItemClickListener = {
//                    startActivityForResult(
//                        DetailActivity.getIntent(this@ListActivity, it.id, DetailMode.DETAIL),
//                        DetailActivity.FETCH_REQUEST_CODE
//                    )
                }, toDoCheckListener = {
                    viewModel.updateEntity(it)
                }
            )
        }
    }




}