package com.example.a3v2.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ToDoList (
    @PrimaryKey(autoGenerate = true)
    val listId      :   Int,
    val isPending   :   Boolean,
    val title       :   String,
    val timestamp   :   String,
    val active      :   Boolean
)