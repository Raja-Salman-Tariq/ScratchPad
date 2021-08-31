package com.example.a3v2

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.a3v2.adapters.MyFragmentPagerAdapter
import com.example.a3v2.databinding.ActivityMainBinding
import com.example.a3v2.db.MyViewModel
import com.example.a3v2.db.MyViewModelFactory
import com.example.a3v2.db.ToDoList
import com.google.android.material.floatingactionbutton.FloatingActionButton

//import com.example.a3v2.db.MyViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var myFragmentPagerAdapter :   MyFragmentPagerAdapter     // fragment+titles list
    private lateinit var myViewPager            :   ViewPager                  // "eagerly" create+manage "pgs"
    private val myViewModel: MyViewModel by viewModels {
        MyViewModelFactory((application as MyApplication).repository)
    }
    private var focus                           :   Int =   0
    private val focus_home_lists                =   0
    private val focus_all_lists                 =   -1



    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        //----------------------
//        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        Log.d("abc", "hafgfhghgndleAddBtn: ")
        binding =   ActivityMainBinding.inflate(layoutInflater)
        handleFrags()
        handleAddBtn()
        handleHomeBtn()
    }

    private fun handleHomeBtn() {
        focus=focus_home_lists
    }

    private fun handleAddBtn() {
        val addBtn  =   findViewById<FloatingActionButton>(R.id.main_activity_add_btn)

        addBtn.setOnClickListener {
            Toast.makeText(this, "working !", Toast.LENGTH_SHORT).show()
            Log.d("abc", "handleAddBtn: ")
            if (focus == focus_home_lists) {
                myViewModel.insertList(ToDoList(0, true, "Another one !", "null"))
            }
        }
    }

    private fun handleFrags() {
        myViewPager             =   findViewById(R.id.main_activity_fragment_container)
        myFragmentPagerAdapter  =   MyFragmentPagerAdapter(supportFragmentManager)
        myFragmentPagerAdapter.addFrag(HomeFragment(myViewModel), "Home")
        myViewPager.adapter     =   myFragmentPagerAdapter
        myViewPager.currentItem =   0
    }
    //-----------------------------------------------

}