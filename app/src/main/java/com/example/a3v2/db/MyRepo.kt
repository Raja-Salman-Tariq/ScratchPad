package com.example.a3v2.db

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single


class MyRepo(
    private val toDoListDao: ToDoListDao,
    private val itemDao: ListItemDao
//    application: Application?,
//    private val listsDao: ToDoListDao   =   MyDB.getDatabase(application!!, GlobalScope)?.toDoListDao(),
//    private val itemDao : ListItemDao   =   MyDB.getDatabase(application!!, GlobalScope)?.listItemDao(),
) {

    val allLists: LiveData<List<ToDoList>> = toDoListDao.getAll()
    val allItems: LiveData<List<ListItem>> = itemDao.getAll()
    val allActiveLists: LiveData<List<ToDoList>> = toDoListDao.getAllActive()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertList(list :   ToDoList) {
        toDoListDao.insertAll(list)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertItem(item :   ListItem) {
        itemDao.insertAll(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateItem(item :   ListItem) {
        itemDao.upd(item)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteList(id :   Int) {
        toDoListDao.deleteById(id)
        itemDao.deleteById(id)
        Log.d("delist", "deleteList: $id ")
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertNewList(list: ToDoList): LiveData<Long> {
        return MutableLiveData(toDoListDao.insert(list))
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deactivateList(id: Int) {
        toDoListDao.deactivateList(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun strikeThroughList(id: Int) {
        toDoListDao.strikeThrough(id)
        itemDao.strikeThrough(id)
    }
}