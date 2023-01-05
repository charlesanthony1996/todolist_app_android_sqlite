package com.example.todolistapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TodoListDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "todo_list_database"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "todos"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TODO_TEXT = "todo_text"
        const val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_NAME ($COLUMN_NAME_ID INTEGER PRIMARY KEY, $COLUMN_NAME_TODO_TEXT TEXT)"
        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun addTodo(todoText: String) {
        val values = ContentValues().apply {
            put(COLUMN_NAME_TODO_TEXT, todoText)
        }
        writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun deleteTodo(todoText: String) {
        writableDatabase.delete(TABLE_NAME, "$COLUMN_NAME_TODO_TEXT = ?", arrayOf(todoText))
    }

    fun updateTodo(oldTodoText: String, newTodoText: String) {
        val values = ContentValues().apply {
            put(COLUMN_NAME_TODO_TEXT, newTodoText)
        }
        writableDatabase.update(TABLE_NAME, values, "$COLUMN_NAME_TODO_TEXT = ?", arrayOf(oldTodoText))
    }

    fun getAllTodos(): Cursor {
        return readableDatabase.rawQuery("SELECT * from $TABLE_NAME", null)
    }
}
