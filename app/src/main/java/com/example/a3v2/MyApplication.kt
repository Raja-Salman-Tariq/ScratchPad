package com.example.a3v2

import android.app.Application
import com.example.a3v2.db.MyDB
import com.example.a3v2.db.MyRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication :   Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { MyDB.getDatabase(this, applicationScope) }
    val repository by lazy { MyRepo(database.toDoListDao(), database.listItemDao()) }
}