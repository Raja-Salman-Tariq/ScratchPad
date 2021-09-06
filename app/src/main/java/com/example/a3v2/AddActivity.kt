package com.example.a3v2

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
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

//    private lateinit var listTitle  :   String
//    private          var listId     :   Int                     =   -1

    private val myViewModel: MyViewModel by viewModels {
        MyViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.hide()

//        listTitle   =   intent.getStringExtra("title").toString()
//        listId      =   intent.getIntExtra("id", -1)

        initViews(/*listId*/)

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

    // spinner menu + recycleview population with selected list's items
    private fun handleSpinner(lists: List<ToDoList>) {
//        if (lists.isNotEmpty()){
//            spinArrow.visibility=View.VISIBLE
//        }

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

    // radio button selection and sending relevant lists to spinner
    private fun handleRadios(){
        spinner.isEnabled=false
        rGroup.check(R.id.radio2)
        rGroup.setOnCheckedChangeListener(object:RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                (this@AddActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(this@AddActivity.window.decorView.rootView?.windowToken, 0)
                rvCard.visibility = View.VISIBLE
                when (rGroup.checkedRadioButtonId){
                    R.id.radio0 ->  {
                        myViewModel.allActiveLists.value?.let {
                            if (it.isNotEmpty()) {
                                spinner.isEnabled = true
                                spinner.visibility = View.VISIBLE
                                spinArrow.visibility = View.VISIBLE
                                recycleView.visibility = View.VISIBLE
                                handleSpinner(it)
                            }
                            else{
                                recycleView.visibility=View.GONE
                                spinner.visibility = View.GONE
                                spinArrow.visibility = View.GONE
                            }
                        }
                    }
                    R.id.radio1 ->  {
                        myViewModel.allLists.value?.let {
                            if (it.isNotEmpty()) {
                                spinner.isEnabled = true
                                spinner.visibility = View.VISIBLE
                                spinArrow.visibility = View.VISIBLE
                                recycleView.visibility = View.VISIBLE
                                handleSpinner(it)
                            }
                            else {
                                recycleView.visibility=View.GONE
                                spinner.visibility = View.GONE
                                spinArrow.visibility = View.GONE
                            }
                        }
                    }
                    R.id.radio2 ->  {
                        spinner.isEnabled   =   false
                        rvCard.visibility   =   View.GONE
                    }
                }
            }
        })

    }

    private fun initViews(/*listId : Int*/){
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
        titleEvLayout.hint = "Enter your new list's title."

//        if (listId!=-1){
//            titleEv.setText(listTitle)
//            titleEv.isEnabled=false
//            titleEvLayout.hint=null
//            titleEvLayout.isHintEnabled=false
//
//            rGroup.visibility= View.GONE
//            rvCard.visibility= View.GONE
//            radioCard.visibility= View.GONE
//            seperator.visibility= View.GONE
//            spinner.visibility= View.GONE
//            spinArrow.visibility= View.GONE
//
//
//        }


        handleActionBarButtons()
    }

    // either closes the add activity, or creates new list/items - performs various error checks
    private fun handleActionBarButtons(){
        backBtn.setOnClickListener { finish() }

        doneBtn.setOnClickListener {

            (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this.window.decorView.windowToken, 0)

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

//            if (listId != -1) {
//                    handleAddition(listId.toLong())
//            }
//            else {

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
//            }
        }
    }

    // adds items selected by tapping into db
    private fun handleAddition(id: Long) {

        Log.d("mineidx", "handleAddition: ${id.toInt()}")

//        if (id<=0)  return

        if (/*listId==-1 && */rvAdapter?.selectedItems?.isNotEmpty() == true){
            for (item   :   ListItem    in  rvAdapter?.selectedItems!!)
                myViewModel.insertItem(ListItem(0, id.toInt(), item.text, false))
        }

        val strTokr =   StringTokenizer(contentEv.text.toString(),"\n")

        while (strTokr.hasMoreTokens())
            myViewModel.insertItem(ListItem(0, id.toInt(), strTokr.nextToken(), false))

        finish()
    }
}