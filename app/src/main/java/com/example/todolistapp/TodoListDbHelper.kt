package com.example.todolistapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TodoListDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun insertTodo(todoText: String, cost: Double) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(TodoListContract.TodoListEntry.COLUMN_NAME_TODO_TEXT, todoText)
        values.put(TodoListContract.TodoListEntry.COLUMN_NAME_COST, cost)
        db.insert(TodoListContract.TodoListEntry.TABLE_NAME, null, values)
    }

    fun deleteTodo(todoText: String) {
        val db = writableDatabase
        val selection = "${TodoListContract.TodoListEntry.COLUMN_NAME_TODO_TEXT} LIKE ?"
        val selectionArgs = arrayOf(todoText)
        db.delete(TodoListContract.TodoListEntry.TABLE_NAME, selection, selectionArgs)
    }

    companion object {
        const val DATABASE_NAME = "todolist.db"
        const val DATABASE_VERSION = 1
        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${TodoListContract.TodoListEntry.TABLE_NAME} (" +
                    "${TodoListContract.TodoListEntry.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
                    "${TodoListContract.TodoListEntry.COLUMN_NAME_TODO_TEXT} TEXT," +
                    "${TodoListContract.TodoListEntry.COLUMN_NAME_COST} REAL)"
        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TodoListContract.TodoListEntry.TABLE_NAME}"
    }
}
