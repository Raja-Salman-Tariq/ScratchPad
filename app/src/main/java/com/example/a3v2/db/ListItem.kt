package com.example.a3v2.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListItem (
    @PrimaryKey(autoGenerate = true)
        val itemId      :   Int,
    val listId      :   Int,
    val text        :   String,
    var strikedOut  :   Boolean
)

