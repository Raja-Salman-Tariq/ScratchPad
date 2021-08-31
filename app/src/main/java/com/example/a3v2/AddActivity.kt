package com.example.a3v2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.adapters.InnerListAdapter
import com.example.a3v2.db.ListItem
import com.example.a3v2.db.MyViewModel
import com.example.a3v2.db.MyViewModelFactory
import com.example.a3v2.db.ToDoList
import com.google.android.material.textfield.TextInputEditText

class AddActivity : AppCompatActivity() {

    private lateinit var titleEv    :   EditText
    private lateinit var rGroup     :   RadioGroup
    private lateinit var rvCard     :   CardView
    private lateinit var radioCard  :   CardView
    private lateinit var seperator  :   ImageView
    private lateinit var contentEv  :   TextInputEditText
    private lateinit var spinner    :   Spinner
    private lateinit var spinArrow  :   ImageView
    private lateinit var recycleView:   RecyclerView
    private          var data       :   MutableList<ListItem>   =   mutableListOf()
    private lateinit var rvAdapter  :   InnerListAdapter

    private val myViewModel: MyViewModel by viewModels {
        MyViewModelFactory((application as MyApplication).repository)
    }

    var adapter         :   ArrayAdapter<String>?   =   null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.hide()

        initViews()

//        myViewModel.allLists.observe(this){
//                lists   ->  handleSpinner(lists)
//        }

        handleSpinner(mutableListOf())

        handleRadios()  // in turn handles spinner which in turn handles rv

    }

    private fun handleRv(listId :   Int) {

        recycleView.layoutManager  =   LinearLayoutManager(this)

        rvAdapter       =   InnerListAdapter(null, this, data)

        recycleView.adapter=   rvAdapter

        myViewModel.allItems.observe(this){
                items   ->  rvAdapter.setData(items, listId)
        }

    }

    private fun handleSpinner(lists: List<ToDoList>) {
        if (lists.isNotEmpty())
            spinArrow.visibility=View.VISIBLE

        val data    =   ArrayList<String>()
        for (todolist:ToDoList  in  lists){
            data.add(todolist.title)
        }

        val spinnerAdapter  =   ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, data.toTypedArray())
        spinner.adapter     =   spinnerAdapter


        spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                handleRv(position+1)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }


    private fun handleRadios(){
        spinner.isEnabled=false
        rGroup.check(R.id.radio2)
        rGroup.setOnCheckedChangeListener(object:RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                when (rGroup.checkedRadioButtonId){
                    R.id.radio0 ->  {
                        spinner.isEnabled   =   true
                        rvCard.visibility   =   View.VISIBLE
                        myViewModel.allActiveLists.value?.let { handleSpinner(it) }
                    }
                    R.id.radio1 ->  {
                        spinner.isEnabled   =   true
                        rvCard.visibility   =   View.VISIBLE
                        myViewModel.allLists.value?.let { handleSpinner(it) }
                    }
                    R.id.radio2 ->  {
                        spinner.isEnabled   =   false
                        rvCard.visibility   =   View.GONE
                    }
                }
            }
        })

    }

    private fun initViews(){
        titleEv     =   findViewById(R.id.add_actitivty_title_ev)
        rGroup      =   findViewById(R.id.radioGroup1)
        rvCard      =   findViewById(R.id.add_rv_card)
        radioCard   =   findViewById(R.id.radiocard)
        seperator   =   findViewById(R.id.add_activity_seperator)
        contentEv   =   findViewById(R.id.add_activity_content_et)
        spinner     =   findViewById(R.id.my_spinner)
        spinArrow   =   findViewById(R.id.add_activity_spinner_arrow)
        recycleView =   findViewById(R.id.addRv)
    }
}