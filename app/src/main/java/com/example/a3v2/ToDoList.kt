package com.example.a3v2

import java.sql.Timestamp

data class ToDoList (
    val listId      :   Int,
    val isPending   :   Boolean,
    val title       :   String,
    val timestamp   :   Timestamp?
)