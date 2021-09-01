package com.example.a3v2.db

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyViewModel(private val repository: MyRepo/*application: Application?*/) : /*Android*/ViewModel(/*application!!*/) {
//    private val repository: MyRepo = MyRepo(application)
    val allItems        :   LiveData<List<ListItem>>    =   repository.allItems
    val allLists        :   LiveData<List<ToDoList>>    =   repository.allLists
    val allActiveLists  :   LiveData<List<ToDoList>>    =   repository.allActiveLists
    val recentID        :   MutableLiveData<Long>       =   MutableLiveData(-1)


    fun insertList(list: ToDoList) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertList(list)
    }

    fun insertItem(item: ListItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertItem(item)
    }

    fun updItem(item: ListItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateItem(item)
    }

    fun insertNewList(list:ToDoList):LiveData<Long>{

        viewModelScope.launch(Dispatchers.IO ) {
            recentID.postValue(repository.insertNewList(list).value)
        }

        return recentID
    }

    fun deactivateList(id:Int)  =   viewModelScope.launch(Dispatchers.IO) {
        repository.deactivateList(id)
    }

    fun deleteList(id:Int)  =   viewModelScope.launch(Dispatchers.IO) {
        repository.deleteList(id)
    }

    fun strikeThroughList(id:Int)  =   viewModelScope.launch(Dispatchers.IO) {
        repository.strikeThroughList(id)
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