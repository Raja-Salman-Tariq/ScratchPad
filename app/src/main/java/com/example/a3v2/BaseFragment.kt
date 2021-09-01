package com.example.a3v2

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.adapters.OuterListAdapter
import com.example.a3v2.db.MyViewModel
import com.example.a3v2.db.ToDoList

open class BaseFragment(internal val myViewModel: MyViewModel)  :   Fragment() {
    protected lateinit var recyclerView     :   RecyclerView
    protected lateinit var data             :   MutableList<ToDoList>
              lateinit var adapter          :   OuterListAdapter
    protected lateinit var emptyTxt         :   TextView
}