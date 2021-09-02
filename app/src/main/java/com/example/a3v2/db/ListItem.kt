package com.example.a3v2.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class ListItem (
    @PrimaryKey(autoGenerate = true)
    val itemId      :   Int,
    val listId      :   Int,
    var text        :   String,
    var strikedOut  :   Boolean,
    var timestamp   :   String =       Timestamp((System.currentTimeMillis())).toString(),
    var concrete: Boolean = true
)

{
    fun mkConcrete(myViewModel: MyViewModel, text: String){
        this.text=text
        timestamp=Timestamp((System.currentTimeMillis())).toString()
        concrete = true
        myViewModel.insertItem(this)
    }
    fun formattedTimestamp(): CharSequence? {
        return MyTimestampFormatter(timestamp).formattedTimestamp
    }
}