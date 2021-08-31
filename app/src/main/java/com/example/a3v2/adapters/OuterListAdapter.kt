package com.example.a3v2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.HomeFragment
import com.example.a3v2.R
import com.example.a3v2.db.ListItem
import com.example.a3v2.db.MyViewModel
import com.example.a3v2.db.ToDoList

class OuterListAdapter(private val ctxt : Context,
                       private val fragment: HomeFragment,
                       private var data:MutableList<ToDoList>,
                       private val myViewModel: MyViewModel )
    :    RecyclerView.Adapter<OuterListAdapter.MyViewHolder>(){

    class MyViewHolder(view :   View,
                       ctxt: Context,
                       fragment: HomeFragment,
                       myViewModel: MyViewModel,
                       val chevron          :   ImageView           =   view.findViewById(R.id.rv_list_chevron),
                       val listHead         :   CardView            =   view.findViewById(R.id.rv_list_header),
                       val listTitle        :   TextView            =   view.findViewById(R.id.rv_list_header_title),
                       val items            :   ConstraintLayout    =   view.findViewById(R.id.rv_list_items_expansion),
                       private val innerList        :   RecyclerView        =   view.findViewById(R.id.rv_list_items),
                       private val innerListData    :   MutableList<ListItem>   =   mutableListOf(),
                       private val adapter          :   InnerListAdapter        =   InnerListAdapter(ctxt, innerListData)
    )   :   RecyclerView.ViewHolder(view){
        init {

            innerList.layoutManager =   LinearLayoutManager(ctxt)
            innerList.adapter       =   adapter
            chevron.rotation        =   180f
            items.visibility        =   View.GONE

            myViewModel.allItems.observe(fragment.viewLifecycleOwner){
                    items   ->  adapter.setData(items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.outerlist_layout, parent, false),
            ctxt,
            fragment,
            myViewModel
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val toDoList    : ToDoList =   data[position]
        holder.listTitle.text           =   toDoList.title

        if (toDoList.isPending){
            holder.listHead.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.blue))
            holder.listTitle.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
        }
        else{
            holder.listHead.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.red))
            holder.listTitle.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
        }

        holder.listHead.setOnClickListener {
            when (holder.items.visibility){
                View.GONE       ->  {
                    holder.items.visibility =   View.VISIBLE
                    holder.chevron.rotation =   0f
                }
                View.VISIBLE    ->  {
                    holder.items.visibility =   View.GONE
                    holder.chevron.rotation =   180f
                }
                View.INVISIBLE ->   {}
            }
        }
    }

    override fun getItemCount() =   data.size

    fun setData(lists: List<ToDoList>?) {
            data= lists as MutableList<ToDoList>
            notifyDataSetChanged()
    }


}