package com.example.todolistapp

import android.provider.BaseColumns

object TodoListContract {

    object TodoListEntry : BaseColumns {
        const val TABLE_NAME = "todos"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TODO_TEXT = "todo_text"
    }
}
