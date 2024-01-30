package com.example.viewpager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpager.R
import com.example.viewpager.models.todoList.TodoTask

class TodoListRecycleViewAdapter(private val todoList: List<TodoTask>): RecyclerView.Adapter<TodoListRecycleViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        fun bind(task: TodoTask) {
            itemView.findViewById<TextView>(R.id.todoListName).text = task.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todoList[position])
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}