package com.example.a3v2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.R
import com.example.a3v2.db.ListItem
import com.example.a3v2.db.ToDoList

class InnerListAdapter(private val ctxt : Context, private var data:MutableList<ListItem>)
    :    RecyclerView.Adapter<InnerListAdapter.MyViewHolder>(){

    class MyViewHolder(view :   View,
                       ctxt : Context,
                       val itemTxt  :   TextView    =   view.findViewById(R.id.rv_innerlist_item),
                       val cardView :   CardView    =   view.findViewById(R.id.rv_innerlist_item_card),
                       val strikeThru:  ImageView   =   view.findViewById(R.id.rv_innerlist_item_strikethrough)
    )   :   RecyclerView.ViewHolder(view){
        init {
            cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.blue))
            strikeThru.visibility=View.INVISIBLE
            itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.innerlist_layout, parent, false), ctxt)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val listItem            =   data[position]
        holder.itemTxt.text     =   listItem.text
        if (listItem.strikedOut){
            holder.itemTxt.tag  =   ctxt.resources.getString(R.string.striked_through)
            holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.red))
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
            holder.strikeThru.visibility=View.VISIBLE
            val params  =   holder.strikeThru.layoutParams
            params.width=   holder.itemTxt.width
            holder.strikeThru.layoutParams=params
            holder.cardView.cardElevation   =0f
            holder.cardView.elevation       =0f
        }else {
            holder.itemTxt.tag  =   ctxt.resources.getString(R.string.not_striked_through)
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.blue))
            holder.strikeThru.visibility=View.INVISIBLE
            holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
        }

        holder.itemTxt.setOnClickListener(View.OnClickListener {
            if (holder.itemTxt.tag.equals(ctxt.resources.getString(R.string.not_striked_through))) {    // clicked to strike through
                holder.itemTxt.tag=ctxt.resources.getString(R.string.striked_through)
                holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.red))
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
                holder.strikeThru.visibility=View.VISIBLE
                val params  =   holder.strikeThru.layoutParams
                params.width=   holder.itemTxt.width
                holder.strikeThru.layoutParams=params
                holder.cardView.cardElevation   =0f
                holder.cardView.elevation       =0f
            }
            else {                                                                                      // clicked to unstrike through
                holder.itemTxt.tag=ctxt.resources.getString(R.string.not_striked_through)
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.blue))
                holder.strikeThru.visibility=View.INVISIBLE
                holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
            }
        })
    }

    override fun getItemCount() =   data.size
    fun setData(items: List<ListItem>?) {
        data = items as MutableList<ListItem>
        notifyDataSetChanged()
    }


}