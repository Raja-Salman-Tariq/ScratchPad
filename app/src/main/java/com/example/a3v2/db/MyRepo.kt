package com.example.a3v2.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData


class MyRepo(
    private val toDoListDao: ToDoListDao,
    private val itemDao: ListItemDao
//    application: Application?,
//    private val listsDao: ToDoListDao   =   MyDB.getDatabase(application!!, GlobalScope)?.toDoListDao(),
//    private val itemDao : ListItemDao   =   MyDB.getDatabase(application!!, GlobalScope)?.listItemDao(),
) {

    val allLists: LiveData<List<ToDoList>> = toDoListDao.getAll()
    val allItems: LiveData<List<ListItem>> = itemDao.getAll()


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


//    fun getItems(listId :   Int):   LiveData<List<ListItem>> {
//        val items   =   mutableListOf<ListItem>()
//        for (item:ListItem  in allItems.value!!){
//            if (item.listId == listId)
//                items.add(item)
//        }
//
//        val toRet   =   MutableLiveData<List<ListItem>>()
//        toRet.value=items
//        return toRet
//    }
}