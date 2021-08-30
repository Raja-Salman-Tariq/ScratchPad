package com.example.a3v2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.adapters.OuterListAdapter
import com.example.a3v2.databinding.ActivityOuterListBinding

class HomeFragment : Fragment() {

    /*###############################################
    * -----        P R O P E R T I E S         -----*
    * =============================================*/
    private lateinit var binding        :   ActivityOuterListBinding

    private lateinit var recyclerView   :   RecyclerView
    private lateinit var data           :   MutableList<ToDoList>
    private lateinit var ada            :   OuterListAdapter
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
        binding= ActivityOuterListBinding.inflate(layoutInflater)

        handleRv()

//        drinkViewModel.mAllDrinks?.observe(viewLifecycleOwner
//        ) { drinks -> // Update the cached copy of the words in the adapter. Observe live data
//            ada.setDrinksData(drinks)
//        }
        return view
    }
    //-----------------------------------------------





    /*###############################################
    * -----   c o n v e n i e n c e   f u n    -----*
    * =============================================*/
    private fun handleRv() {
        recyclerView    =   binding.fragmentHomeAllListsRv

        data = mutableListOf()
        data.add(ToDoList(0,false, "first list", null))
        data.add(ToDoList(1,false, "second list", null))
        data.add(ToDoList(2,false, "third list", null))
        data.add(ToDoList(3,true, "fourth list", null))
        data.add(ToDoList(4,false, "fifth list", null))


        recyclerView.layoutManager  =   LinearLayoutManager(context)
        (context?.let { OuterListAdapter(it, data) })?.let { ada    =   it }
        recyclerView.adapter        =   ada

    }
    //-----------------------------------------------
}