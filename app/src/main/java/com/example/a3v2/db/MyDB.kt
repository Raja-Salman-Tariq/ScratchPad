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
                ).addCallback(MyDbInitCallback(scope))
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


            val first = ToDoList(0, true, "first list !", "")
            val second = ToDoList(0, false, "second list !", "")
            val third = ToDoList(0, true, "last list !", "")

            Log.d("inserted", "populateDatabase: ")
            listDao.insertAll(first, second, third)


            var _0  =   ListItem(0, 1, "Grocery", false)
            var _1  =   ListItem(0, 1,"Eggs", true)
            var _2  =   ListItem(0, 1, "Tomatos", false)
            var _3  =   ListItem(0, 1, "Saag", true)

            itemDao.insertAll(_0,_1,_2,_3)

            _1  =   ListItem(0, 3,"Eggs", false)
            _2  =   ListItem(0, 3, "Tomatos", false)
            _3  =   ListItem(0, 3, "Saag", true)
            itemDao.insertAll(_1,_2,_3)


            _0  =   ListItem(0, 2, "Grocery", true)
            itemDao.insertAll(_0)

            // Add sample words.
//            var word = Word("Hello")
//            wordDao.insert(word)
//            word = Word("World!")
//            wordDao.insert(word)
//
        }
    }

}