package com.example.a3v2.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyViewModel(private val repository: MyRepo/*application: Application?*/) : /*Android*/ViewModel(/*application!!*/) {
//    private val repository: MyRepo = MyRepo(application)
    val allItems        :   LiveData<List<ListItem>>    =   repository.allItems
    val allLists        :   LiveData<List<ToDoList>>    =   repository.allLists
    val allActiveLists  :   LiveData<List<ToDoList>>    =   repository.allActiveLists


    fun insertList(list: ToDoList) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertList(list)
    }

    fun insertItem(item: ListItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertItem(item)
    }

    fun updItem(item: ListItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateItem(item)
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