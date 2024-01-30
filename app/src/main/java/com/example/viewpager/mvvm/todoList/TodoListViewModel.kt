package com.example.viewpager.mvvm.todoList

import androidx.lifecycle.ViewModel
import com.example.viewpager.models.todoList.TodoTask
import com.example.viewpager.models.todoList.TodoTaskState
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject

class TodoListViewModel(private var repository: TodoListRepository = TodoListRepository()): ViewModel() {
    val todoListDataSubject = PublishSubject.create<List<TodoTask>>()
    private var _destroySubject = ReplaySubject.create<Int>()
    var activeTodoState: TodoTaskState = TodoTaskState.NONE
        set(value) {
            field = value
            repository.loadTodoListTasksByStatus(value)
                .takeUntil(_destroySubject)
                .subscribe(object:Observer<List<TodoTask>> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(t: List<TodoTask>) {
                       todoListDataSubject.onNext(t)
                    }

                    override fun onError(e: Throwable) {}

                    override fun onComplete() {}
                })
        }

    override fun onCleared() {
        super.onCleared()
        _destroySubject.onNext(1)
        _destroySubject.onComplete()
    }
}