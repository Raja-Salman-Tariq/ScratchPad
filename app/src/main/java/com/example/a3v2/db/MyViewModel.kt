package com.example.a3v2.db

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyViewModel(private val repository: MyRepo/*application: Application?*/) : /*Android*/ViewModel(/*application!!*/) {
//    private val repository: MyRepo = MyRepo(application)
    val allItems    :   LiveData<List<ListItem>>    =   repository.allItems
    val allLists    :   LiveData<List<ToDoList>>    =   repository.allLists


    fun insertList(list: ToDoList) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertList(list)
    }

    fun insertItem(item: ListItem) = viewModelScope.launch {
        repository.insertItem(item)
    }
}


class MyViewModelFactory(private val repository: MyRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}