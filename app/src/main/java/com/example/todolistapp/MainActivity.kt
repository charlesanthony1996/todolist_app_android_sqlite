package com.example.todolistapp

import android.app.Activity
import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*

class MainActivity : Activity() {

    private lateinit var dbHelper: TodoListDbHelper
    private lateinit var todoList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = TodoListDbHelper(this)
        todoList = ArrayList()
        adapter = ArrayAdapter(this, R.layout.list_item, R.id.todo_text, todoList)
        todo_list.adapter = adapter

        add_todo_button.setOnClickListener {
            addTodo()
        }

        todo_list.setOnItemClickListener { _, view, _, _ ->
            view.delete_button.setOnClickListener {
                val todoText = view.todo_text.text.toString()
                deleteTodo(todoText)
            }
        }

        getAllTodos()
    }



    private fun addTodo() {
        val todoText = new_todo_edit_text.text.toString()
        val cost = todo_cost_edit_text.text.toString().toDouble()
        if (todoText.isNotEmpty()) {
            dbHelper.insertTodo(todoText, cost)
            new_todo_edit_text.text.clear()
            todo_cost_edit_text.text.clear()
            Toast.makeText(this, "Todo added!", Toast.LENGTH_SHORT).show()
            getAllTodos()
            updateTotalCost()
        } else {
            Toast.makeText(this, "Please enter a todo", Toast.LENGTH_SHORT).show()
        }
    }


    private fun deleteTodo(todoText: String) {
        dbHelper.deleteTodo(todoText)
        getAllTodos()
        updateTotalCost()
    }


    private fun getAllTodos() {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(TodoListContract.TodoListEntry.COLUMN_NAME_ID, TodoListContract.TodoListEntry.COLUMN_NAME_TODO_TEXT)
        val cursor: Cursor = db.query(TodoListContract.TodoListEntry.TABLE_NAME, projection, null, null, null, null, null)

        todoList.clear()
        while (cursor.moveToNext()) {
            val columnIndex = cursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_NAME_TODO_TEXT)
            if (columnIndex != -1) {
                val todoText = cursor.getString(columnIndex)
                todoList.add(todoText)
            }
        }

        adapter.notifyDataSetChanged()
        cursor.close()
    }


    private fun updateTotalCost() {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(TodoListContract.TodoListEntry.COLUMN_NAME_ID, TodoListContract.TodoListEntry.COLUMN_NAME_COST)
        val cursor: Cursor = db.query(TodoListContract.TodoListEntry.TABLE_NAME, projection, null, null, null, null, null)

        var totalCost = 0.0
        while (cursor.moveToNext()) {
            val columnIndex = cursor.getColumnIndex(TodoListContract.TodoListEntry.COLUMN_NAME_COST)
            if (columnIndex != -1) {
                val cost = cursor.getDouble(columnIndex)
                totalCost += cost
            }
        }

        total_cost_text_view.text = "Total Cost: $${totalCost}"
        cursor.close()
    }


}
