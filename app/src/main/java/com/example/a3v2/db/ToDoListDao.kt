package com.example.a3v2.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ToDoListDao {

    @Query("SELECT * FROM ToDoList")
    fun getAll(): LiveData<List<ToDoList>>

    @Query("SELECT * FROM ToDoList WHERE active = 1")
    fun getAllActive(): LiveData<List<ToDoList>>

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert
    fun insertAll(vararg lists: ToDoList)

    @Insert
    fun insert(list: ToDoList) : Long


    @Delete
    fun delete(list: ToDoList)

    @Query("UPDATE ToDoList SET isPending = :listIsPending WHERE listId = :id")
    fun updListState(id: Int, listIsPending: Boolean)

    @Query("DELETE FROM ToDoList")
    suspend fun deleteAll()

    @Query("UPDATE ToDoList SET active=0 WHERE listId=:id")
    suspend fun deactivateList(id: Int)

    @Query("DELETE FROM ToDoList WHERE listId = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE ToDoList SET isPending = not :isPending WHERE listId=:id")
    suspend fun strikeThrough(id: Int, isPending : Boolean)
}