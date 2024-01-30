package com.example.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpager.adapters.TodoListRecycleViewAdapter
import com.example.viewpager.models.todoList.TodoTaskState
import com.example.viewpager.mvvm.todoList.TodoListViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

const val TODO_LIST_STATE = "state"
class TodoListFragment : Fragment() {
    private lateinit var status: TodoTaskState
    private lateinit var listRV: RecyclerView
    private val disposable = CompositeDisposable()
    private val _viewModel: TodoListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val status = arguments?.getString(TODO_LIST_STATE)
        status?.let {
            this.status = TodoTaskState.valueOf(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listRV = view.findViewById(R.id.todoListRv)
        listRV.layoutManager = LinearLayoutManager(this.context)
        disposable.add(
            _viewModel.todoListDataSubject
                    .filter { list -> (list.any { it.status == status }) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                listRV.adapter = TodoListRecycleViewAdapter(it)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(todoListState: TodoTaskState) =
            TodoListFragment().apply {
                arguments = Bundle().apply {
                    putString(TODO_LIST_STATE, todoListState.toString())
                }
            }
    }
}