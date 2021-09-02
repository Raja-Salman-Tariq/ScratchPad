package com.example.a3v2.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.*

@Entity
data class ToDoList(
    @PrimaryKey(autoGenerate = true)
    val listId:   Int,
    val isPending:   Boolean,
    val title:   String,
    val active:   Boolean,
    val timestamp: String =       Timestamp((System.currentTimeMillis())).toString(),
) {
    fun formattedTimestamp(): CharSequence? {
        return MyTimestampFormatter(timestamp).formattedTimestamp
    }
}