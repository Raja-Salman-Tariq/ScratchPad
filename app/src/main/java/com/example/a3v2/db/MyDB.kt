package com.example.a3v2.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ToDoList::class, ListItem::class], version = 1, exportSchema = true)
abstract class MyDB : RoomDatabase() {

    abstract fun toDoListDao()  :   ToDoListDao
    abstract fun listItemDao()  :   ListItemDao

    companion object {
        @Volatile
        private var INSTANCE: MyDB? = null

        fun getDatabase(context: Context, scope:CoroutineScope): MyDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDB::class.java,
                    "my_database"
                )//.addCallback(MyDbInitCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class MyDbInitCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.toDoListDao(), database.listItemDao())
                    }
                }
            }
        }



        suspend fun populateDatabase(listDao: ToDoListDao, itemDao: ListItemDao) {
            // Delete all content here.
            listDao.deleteAll()
            itemDao.deleteAll()


            val first = ToDoList(0, true, "first list !",true)
            val second = ToDoList(0, false, "second list !", false)
            val third = ToDoList(0, true, "last list !", false)

            Log.d("inserted", "populateDatabase: ")
            listDao.insertAll(first, second, third)


            var v0  =   ListItem(0, 1, "Grocery", false)
            var v1  =   ListItem(0, 1,"Eggs", true)
            var v2  =   ListItem(0, 1, "Tomatos", false)
            var v3  =   ListItem(0, 1, "Saag", true)

            itemDao.insertAll(v0,v1,v2,v3)

            v1  =   ListItem(0, 3,"Eggs", false)
            v2  =   ListItem(0, 3, "Tomatos", false)
            v3  =   ListItem(0, 3, "Saag", true)
            itemDao.insertAll(v1,v2,v3)


            v0  =   ListItem(0, 2, "Grocery", true)
            itemDao.insertAll(v0)

        }
    }

}