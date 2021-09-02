package com.example.a3v2

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.adapters.InnerListAdapter
import com.example.a3v2.db.ListItem
import com.example.a3v2.db.MyViewModel
import com.example.a3v2.db.MyViewModelFactory
import com.example.a3v2.db.ToDoList
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {

    private lateinit var titleEv    :   TextInputEditText
    private lateinit var rGroup     :   RadioGroup
    private lateinit var rvCard     :   CardView
    private lateinit var radioCard  :   CardView
    private lateinit var seperator  :   ImageView
    private lateinit var inpLayout  :   TextInputLayout
    private lateinit var contentEv  :   TextInputEditText
    private lateinit var spinner    :   Spinner
    private lateinit var spinArrow  :   ImageView
    private lateinit var recycleView:   RecyclerView
    private          var data       :   MutableList<ListItem>   =   mutableListOf()
    private          var rvAdapter  :   InnerListAdapter?       =   null
    private lateinit var backBtn    :   ImageView
    private lateinit var doneBtn    :   ImageView

    private lateinit var listTitle  :   String
    private          var listId     :   Int                     =   -1

    private val myViewModel: MyViewModel by viewModels {
        MyViewModelFactory((application as MyApplication).repository)
    }

    var adapter         :   ArrayAdapter<String>?   =   null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.hide()

        listTitle   =   intent.getStringExtra("title").toString()
        listId      =   intent.getIntExtra("id", -1)

        initViews(listId)

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
                items   ->  rvAdapter?.setData(items, listId)
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
                handleRv(lists[position].listId)
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
                        recycleView.visibility = View.VISIBLE
                        myViewModel.allActiveLists.value?.let { handleSpinner(it) }
                    }
                    R.id.radio1 ->  {
                        spinner.isEnabled   =   true
                        rvCard.visibility   =   View.VISIBLE
                        recycleView.visibility = View.VISIBLE
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

    private fun initViews(listId : Int){
        titleEv     =   findViewById(R.id.add_actitivty_title_ev)
        rGroup      =   findViewById(R.id.radioGroup1)
        rvCard      =   findViewById(R.id.add_rv_card)
        radioCard   =   findViewById(R.id.radiocard)
        seperator   =   findViewById(R.id.add_activity_seperator)
        contentEv   =   findViewById(R.id.add_activity_content_et)
        inpLayout   =   findViewById(R.id.text_input_layout)
        spinner     =   findViewById(R.id.my_spinner)
        spinArrow   =   findViewById(R.id.add_activity_spinner_arrow)
        recycleView =   findViewById(R.id.addRv)
        backBtn     =   findViewById(R.id.add_activity_back)
        doneBtn     =   findViewById(R.id.add_activity_add_btn)


        val titleEvLayout   =    findViewById<TextInputLayout>(R.id.name_text_input_layout)

        titleEvLayout.isHintEnabled=true
        titleEvLayout.hint = "Enter your new list's title here."

        if (listId!=-1){
            titleEv.setText(listTitle)
            titleEv.isEnabled=false
            titleEvLayout.hint=null
            titleEvLayout.isHintEnabled=false

            rGroup.visibility= View.GONE
            rvCard.visibility= View.GONE
            radioCard.visibility= View.GONE
            seperator.visibility= View.GONE
            spinner.visibility= View.GONE
            spinArrow.visibility= View.GONE


        }


        handleActionBarButtons()

//        contentEv.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> contentEv.error=null }
    }

    private fun handleActionBarButtons(){
        backBtn.setOnClickListener { finish() }

        doneBtn.setOnClickListener {

            if (titleEv.text.isNullOrEmpty()) {
                titleEv.error = "Please enter new list name to continue."
                Log.d("addbtn", "handleActionBarButtons:22222222 ")
                return@setOnClickListener
            }
            if ((rvAdapter?.selectedItems?.isEmpty() == true || rvAdapter == null) && contentEv.text.isNullOrEmpty()) {
                contentEv.error = "No Items Selected For New List.\n\n" +
                        "Enter your items line seperated here, or choose items from an exiting list" +
                        " by using the radio buttons, drop down, and corresponding item list."
                Log.d("addbtn", "handleActionBarButtons:22222222 ")
                return@setOnClickListener
            }

            if (listId != -1) {
//                Log.d("addbtn", "cond: ${titleEv.text.isNullOrEmpty()}" +
//                        "val: ${titleEv.text} ")

//                if (titleEv.text.isNullOrEmpty()) {
//                    titleEv.error = "Please enter new items to continue"
//                    Log.d("addbtn", "handleActionBarButtons:22222222 ")
//                }
//                else
                    handleAddition(listId.toLong())
            }
            else {
//            Log.d("addbtn", "handleActionBarButtons: ")


//                if ((rvAdapter?.selectedItems?.isEmpty() == true || rvAdapter == null) && contentEv.text.isNullOrEmpty()) {
//                    contentEv.error = "No Items Selected For New List.\n\n" +
//                            "Enter your items line seperated here, or choose items from an exiting list" +
//                            " by using the radio buttons, drop down, and corresponding item list."
//                    Log.d("addbtn", "handleActionBarButtons:22222222 ")
//                }
//                else if (titleEv.text.isNullOrEmpty()){
//                    titleEv.error="You need to name your list"
//                }

//                else {
                    myViewModel.insertNewList(
                        ToDoList(
                            0,
                            true,
                            titleEv.text.toString(),
                            true
                        ),
                    ).observe(this) { id ->
                        handleAddition(id)
                    }
//                }

            }
        }
    }

    private fun handleAddition(id: Long) {

        Log.d("mineidx", "handleAddition: ${id.toInt()}")

        if (id<=0)  return

        if (listId==-1 && rvAdapter?.selectedItems?.isNotEmpty() == true){
            for (item   :   ListItem    in  rvAdapter?.selectedItems!!)
                myViewModel.insertItem(ListItem(0, id.toInt(), item.text, false))
        }

        val strTokr =   StringTokenizer(contentEv.text.toString(),"\n")

        while (strTokr.hasMoreTokens())
            myViewModel.insertItem(ListItem(0, id.toInt(), strTokr.nextToken(), false))

        finish()
    }
}