package com.example.a3v2.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.R
import com.example.a3v2.BaseFragment
import com.example.a3v2.db.ListItem

class InnerListAdapter(private val fragment: BaseFragment, private val ctxt : Context, private var data:MutableList<ListItem>)
    :    RecyclerView.Adapter<InnerListAdapter.MyViewHolder>(){

    class MyViewHolder(view :   View,
                       val itemTxt  :   TextView    =   view.findViewById(R.id.rv_innerlist_item),
                       val cardView :   CardView    =   view.findViewById(R.id.rv_innerlist_item_card),
                       val strikeOut:  ImageView   =   view.findViewById(R.id.rv_innerlist_item_strikethrough)
    )   :   RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.innerlist_layout, parent, false))
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val listItem            =   data[position]
        holder.itemTxt.text     =   listItem.text
        if (listItem.strikedOut){
            holder.itemTxt.tag  =   ctxt.resources.getString(R.string.striked_through)
            holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.red))
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
            holder.strikeOut.visibility=View.VISIBLE
            val params  =   holder.strikeOut.layoutParams
            params.width=   holder.itemTxt.width+25
            holder.strikeOut.layoutParams=params
            holder.strikeOut.requestLayout()
        }else {
            holder.itemTxt.tag  =   ctxt.resources.getString(R.string.not_striked_through)
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.blue))
            holder.strikeOut.visibility=View.INVISIBLE
            holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
        }

        setListeners(holder, listItem)
    }

    override fun getItemCount() =   data.size

    fun setData(items   :   List<ListItem>, listId  :   Int): Boolean {
        Log.d("xyz", "setData: $items")
        data.clear()

        var listIsPending   =   false

        for (item   : ListItem  in  items)
            if (item.listId==listId) {
                data.add(item)
                if (!item.strikedOut)
                    listIsPending   =   true
            }

        notifyDataSetChanged()
        return listIsPending
    }

    private fun setListeners(holder: MyViewHolder, item: ListItem){
        holder.cardView.setOnClickListener(View.OnClickListener {

            if (holder.itemTxt.tag.equals(ctxt.resources.getString(R.string.not_striked_through))) {    // clicked to strike through
                holder.itemTxt.tag=ctxt.resources.getString(R.string.striked_through)
                holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.red))
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
                holder.strikeOut.visibility=View.VISIBLE
                val params  =   holder.strikeOut.layoutParams
                params.width=   holder.itemTxt.width+25
                holder.strikeOut.layoutParams=params
                item.strikedOut =   true
            }
            else {                                                                                      // clicked to unstrike through
                holder.itemTxt.tag=ctxt.resources.getString(R.string.not_striked_through)
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.blue))
                holder.strikeOut.visibility=View.INVISIBLE
                holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
                item.strikedOut =   false
            }
            fragment.myViewModel.updItem(item)
        })


    }


}