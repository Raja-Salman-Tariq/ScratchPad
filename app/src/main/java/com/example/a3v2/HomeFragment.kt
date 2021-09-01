package com.example.a3v2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.adapters.OuterListAdapter
import com.example.a3v2.databinding.FragmentHomeBinding
import com.example.a3v2.db.MyViewModel
import com.example.a3v2.db.ToDoList


class HomeFragment(myViewModel: MyViewModel) : BaseFragment(myViewModel) {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/
    private lateinit var binding        :   FragmentHomeBinding
    //-----------------------------------------------


    /*###############################################
    * -----      I N I T   &   S E T U P       -----*
    * =============================================*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        binding= FragmentHomeBinding.inflate(layoutInflater)

        return view
    }
    //-----------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleRv()
    }



    /*###############################################
    * -----   c o n v e n i e n c e   f u n    -----*
    * =============================================*/
    private fun handleRv() {
        emptyTxt        =   view?.findViewById(R.id.fragment_home_empty_txt)!!
        recyclerView    = view?.findViewById(R.id.fragment_home_all_lists_rv)!!
//        recyclerView    =   binding.fragmentHomeAllListsRv

        data = mutableListOf()

        recyclerView.layoutManager  =   LinearLayoutManager(context)
        adapter =   OuterListAdapter(requireContext(), this, data, myViewModel)
        recyclerView.adapter        =   adapter


        myViewModel.allActiveLists.observe(viewLifecycleOwner){
                lists   ->  observeHomeFragmentData(lists)
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
//                Toast.makeText(context, "on Swiped $swipeDir", Toast.LENGTH_SHORT).show()
                //Remove swiped item from list and notify the RecyclerView
                myViewModel.deactivateList(adapter.getListId(viewHolder.adapterPosition))
                (this@HomeFragment.activity as MainActivity).apply {
                    focus           =   -1
                    myTitle.text    =   resources.getString(R.string.home_title)
                }
                adapter.notifyDataSetChanged()
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }
    //-----------------------------------------------
    private fun observeHomeFragmentData(lists   : List<ToDoList>){
        if (lists.isEmpty()){
            emptyTxt.visibility =   View.VISIBLE
        }
        else
            emptyTxt.visibility =   View.GONE
        adapter.setMyData(lists)
    }
}