package com.example.a3v2.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ToDoListDao {

    @Query("SELECT * FROM ToDoList")
    fun getAll(): LiveData<List<ToDoList>>

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert
    fun insertAll(vararg lists: ToDoList)

    @Delete
    fun delete(list: ToDoList)

    @Update
    fun upd(list: ToDoList)

    @Query("DELETE FROM ToDoList")
    suspend fun deleteAll()
}