package com.example.a3v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.adapters.InnerListAdapter
import com.example.a3v2.adapters.OuterListAdapter
import com.example.a3v2.databinding.ActivityOuterListBinding

class OuterListActivity : AppCompatActivity() {

    private lateinit var binding        :   ActivityOuterListBinding

    private lateinit var recyclerView   :   RecyclerView
    private lateinit var data           :   MutableList<ToDoList>
    private lateinit var ada            :   OuterListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOuterListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        handleRv()
    }

    private fun handleRv() {
        recyclerView    =   binding.fragmentHomeAllListsRv
        data            =   mutableListOf()

        data = mutableListOf()
        data.add(ToDoList(0,true, "first list", null))
        data.add(ToDoList(1,true, "second list", null))
        data.add(ToDoList(2,false, "third list", null))
        data.add(ToDoList(3,true, "fourth list", null))
        data.add(ToDoList(4,false, "fifth list", null))


        recyclerView.layoutManager  =   LinearLayoutManager(this)
        ada                         =   OuterListAdapter(this, data)
        recyclerView.adapter        =   ada

    }
}