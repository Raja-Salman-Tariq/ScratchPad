package com.example.a3v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.adapters.InnerListAdapter
import com.example.a3v2.databinding.ActivityInnerListBinding

class InnerListActivity : AppCompatActivity() {

    private lateinit var binding        :   ActivityInnerListBinding

    private lateinit var recyclerView   :   RecyclerView
    private lateinit var data           :   MutableList<String>
    private lateinit var ada            :   InnerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityInnerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleRv()
    }

    private fun handleRv() {
        recyclerView    =   binding.rvListItemsInnerListAct
        data            =   mutableListOf()

        data = mutableListOf()
        data.add("list 1")
        data.add("list 2")
        data.add("list 3")
        data.add("list 4")
        data.add("list 5")


        recyclerView.layoutManager  =   LinearLayoutManager(this)
        ada                         =   InnerListAdapter(this, data)
        recyclerView.adapter        =   ada

    }
}