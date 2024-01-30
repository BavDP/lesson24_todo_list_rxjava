package com.example.viewpager.mvvm.todoList

import com.example.viewpager.models.todoList.TodoTask
import com.example.viewpager.models.todoList.TodoTaskDB
import com.example.viewpager.models.todoList.TodoTaskState
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Random
import java.util.concurrent.TimeUnit
import io.reactivex.rxjava3.core.Observable

class TodoListRepository() {
    fun loadTodoListTasksByStatus(status: TodoTaskState): Observable<List<TodoTask>> {
        val delayPause = Random(0).nextInt(5000).toLong()
        return io.reactivex.rxjava3.core.Observable.just(TodoTaskDB.tasks())
            .map { data -> data.filter { task -> task.status == status } }
            // network request pause imitation
            .delay(delayPause, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
    }
}