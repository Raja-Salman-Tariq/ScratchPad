package com.example.a3v2.adapters

import android.content.Context
import android.util.Log
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
import com.example.a3v2.R
import com.example.a3v2.BaseFragment
import com.example.a3v2.db.ListItem
import com.example.a3v2.db.MyViewModel
import com.example.a3v2.db.ToDoList
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.a3v2.MainActivity


class OuterListAdapter(private val ctxt : Context,
                       private val fragment: BaseFragment,
                               var data:MutableList<ToDoList>,
                       private val myViewModel: MyViewModel )
    :    RecyclerView.Adapter<OuterListAdapter.MyViewHolder>(){

    class MyViewHolder(
        view: View,
        ctxt: Context,
        fragment: BaseFragment,
        data    : MutableList<ToDoList>,
        val chevron: ImageView = view.findViewById(R.id.rv_list_chevron),
        val listHead: CardView = view.findViewById(R.id.rv_list_header),
        val headerStrikeOut: ImageView = view.findViewById(R.id.rv_outerlist_item_strikethrough),
        val listTitle: TextView = view.findViewById(R.id.rv_list_header_title),
        val items: ConstraintLayout = view.findViewById(R.id.rv_list_items_expansion),
        private val innerList: RecyclerView = view.findViewById(R.id.rv_list_items),
        private val innerListData: MutableList<ListItem> = mutableListOf(),
        val adapter: InnerListAdapter = InnerListAdapter(fragment, ctxt, innerListData),
    )   :   RecyclerView.ViewHolder(view){
        init {

            innerList.layoutManager =   LinearLayoutManager(ctxt)
            innerList.adapter       =   adapter
            chevron.rotation        =   180f
            items.visibility        =   View.GONE
            headerStrikeOut.visibility  =   View.GONE

            val mScrollTouchListener: OnItemTouchListener = object : OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    when (e.action) {
                        MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            }

            innerList.addOnItemTouchListener(mScrollTouchListener)

            listHead.setOnLongClickListener {
                Log.d("longClick", "doing: ")
                Toast.makeText(ctxt, "long clicked", Toast.LENGTH_SHORT).show()
                fragment.myViewModel.strikeThroughList(data[adapterPosition].listId)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.outerlist_layout, parent, false),
            ctxt,
            fragment,
            data
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
            val myActivity:   MainActivity    =   (fragment.activity as MainActivity)
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

            myActivity.focus    =   holder.adapterPosition
            myActivity.myTitle.text  =   toDoList.title
        }

        myViewModel.allItems.observe(fragment.viewLifecycleOwner){
                items   ->  observe(holder, items, toDoList.listId)
        }
    }

    override fun getItemCount() =   data.size

    fun setMyData(lists: List<ToDoList>?) {
            data= lists as MutableList<ToDoList>
            notifyDataSetChanged()
    }

    private fun observe(holder:MyViewHolder, items: List<ListItem>, listId:Int){
        val listIsPending   =   holder.adapter.setData(items, listId )
        if (!listIsPending){
            holder.headerStrikeOut.visibility   =   View.VISIBLE
            val params      = holder.headerStrikeOut.layoutParams
            params.width    = holder.listTitle.width
            holder.headerStrikeOut.layoutParams =   params
//            holder.listTitle.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
            holder.listHead.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.red))
        }
        else{
            holder.headerStrikeOut.visibility   =   View.INVISIBLE
//            holder.listTitle.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
            holder.listHead.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.blue))
        }
    }

    fun getListId(pos:Int)=data[pos].listId
}