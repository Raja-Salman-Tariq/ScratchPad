package com.example.a3v2.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ListItemDao {

    @Query("SELECT * FROM ListItem")
    fun getAll(): LiveData<List<ListItem>>

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert
    fun insertAll(vararg items: ListItem)

    @Delete
    fun delete(item: ListItem)

    @Update
    fun upd(item: ListItem)

    @Query("SELECT * FROM ListItem WHERE listId = :listId")
    fun getItems(listId:Int):   LiveData<List<ListItem>>

    @Query("DELETE FROM ListItem")
    suspend fun deleteAll()

    @Query("DELETE FROM ListItem WHERE listId = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE ListItem SET strikedOut= :isStriked WHERE listId = :id")
    fun strikeThrough(id: Int, isStriked : Boolean)
}