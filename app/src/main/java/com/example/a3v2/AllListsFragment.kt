package com.example.a3v2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.adapters.OuterListAdapter
import com.example.a3v2.databinding.FragmentAllListsBinding
import com.example.a3v2.db.MyViewModel
import com.example.a3v2.db.ToDoList

class AllListsFragment(myViewModel: MyViewModel) : BaseFragment(myViewModel) {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/
    private lateinit var binding        :   FragmentAllListsBinding
    //-----------------------------------------------


    /*###############################################
    * -----      I N I T   &   S E T U P       -----*
    * =============================================*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_all_lists, container, false)
        binding= FragmentAllListsBinding.inflate(layoutInflater)
        return view
    }
    //-----------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).myTitle.text         =   resources.getString(R.string.all_lists_title)
//        (activity as MainActivity).addBtn.visibility    =   View.INVISIBLE
        handleRv()
    }



    /*###############################################
    * -----   c o n v e n i e n c e   f u n    -----*
    * =============================================*/
    private fun handleRv() {
        emptyTxt        =   view?.findViewById(R.id.fragment_all_lists_empty_txt)!!
        recyclerView    =   view?.findViewById(R.id.fragment_all_lists_rv)!!
//        recyclerView    =   binding.fragmentHomeAllListsRv

        data = mutableListOf()

        recyclerView.layoutManager  =   LinearLayoutManager(context)
        adapter =   OuterListAdapter(requireContext(), this, data, myViewModel)
        recyclerView.adapter        =   adapter

        myViewModel.allLists.observe(this.viewLifecycleOwner){
                lists   ->  observeAllListsFragmentData(lists)
        }

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
//                Toast.makeText(context, "on Move", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val id = adapter.getListId(viewHolder.adapterPosition)
//                Toast.makeText(context, "on Swiped $swipeDir against id $id", Toast.LENGTH_SHORT).show()
                //Remove swiped item from list and notify the RecyclerView
                Log.d("abc", "deleteList: sz ${myViewModel.allLists.value?.size}")

                myViewModel.deleteList(id)
                adapter.notifyDataSetChanged()

                Log.d("abc", "deleteList ffteeer: sz ${myViewModel.allLists.value?.size}")

            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    //-----------------------------------------------
    private fun observeAllListsFragmentData(lists   : List<ToDoList>){

        Log.d("observed", "observeAllListsFragmentData: ")

        if (lists.isEmpty()){
            emptyTxt.visibility =   View.VISIBLE
        }
        else
            emptyTxt.visibility =   View.GONE
        adapter.setMyData(lists)
    }
}