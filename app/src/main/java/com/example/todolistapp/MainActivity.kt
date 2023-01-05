package com.example.todolistapp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*

class MainActivity : AppCompatActivity() {

    private val todoList = arrayListOf<String>()
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var db: TodoListDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = TodoListDbHelper(this)
        loadTodos()

        arrayAdapter = ArrayAdapter(this, R.layout.list_item, R.id.todo_text, todoList)
        todo_list.adapter = arrayAdapter

        add_todo_button.setOnClickListener {
            addTodo()
        }
    }

    private fun loadTodos() {
        val cursor = db.getAllTodos()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val todoText = cursor.getString(cursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_NAME_TODO_TEXT))
            todoList.add(todoText)
            cursor.moveToNext()
        }
        cursor.close()
    }

    private fun addTodo() {
        val todoText = new_todo_text.text.toString()
        if (todoText.isNotEmpty()) {
            db.addTodo(todoText)
            todoList.add(todoText)
            arrayAdapter.notifyDataSetChanged()
            new_todo_text.setText("")
        }
    }

    private fun deleteTodo(todoText: String) {
        db.deleteTodo(todoText)
        todoList.remove(todoText)
        arrayAdapter.notifyDataSetChanged()
    }

    private fun updateTodo(oldTodoText: String, newTodoText: String) {
        db.updateTodo(oldTodoText, newTodoText)
        val index = todoList.indexOf(oldTodoText)
        todoList[index] = newTodoText
        arrayAdapter.notifyDataSetChanged()
    }
}
