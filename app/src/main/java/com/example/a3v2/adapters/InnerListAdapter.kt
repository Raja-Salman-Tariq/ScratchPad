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

class InnerListAdapter(private val fragment: BaseFragment?, private val ctxt : Context, private var data:MutableList<ListItem>)
    :    RecyclerView.Adapter<InnerListAdapter.MyViewHolder>(){

    val selectedItems   =   mutableSetOf<ListItem>()

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


        if (fragment==null){ //  frag null means this is second usage of adapter in the add button activity
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
            holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.blue))
            holder.strikeOut.visibility=View.INVISIBLE
            holder.cardView.setOnClickListener(object:View.OnClickListener{
                override fun onClick(p0: View?) {
                    val item    :   ListItem    =   data[holder.adapterPosition]
                    if (selectedItems.contains(item)){
                        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
                        holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.blue))
                        selectedItems.remove(item)
                    }

                    else {
                        selectedItems.add(item)
                        holder.cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                ctxt,
                                R.color.blue
                            )
                        )
                        holder.itemTxt.setTextColor(
                            ContextCompat.getColor(
                                ctxt,
                                R.color.deeper_white_alt
                            )
                        )
                    }
                }

            })
            return
        }

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
        if (fragment==null)
            Log.d("xyz", "setData: ${items.size}\n$items")

        data.clear()

        var listIsPending   =   false

        for (item   : ListItem  in  items)
            if (item.listId==listId) {
                data.add(item)
                if (!item.strikedOut)
                    listIsPending   =   true
            }

        data.sortWith(compareBy<ListItem>{it.strikedOut}.thenBy { it.timestamp })

        notifyDataSetChanged()
        return listIsPending
    }

    private fun setListeners(holder: MyViewHolder, item: ListItem){
        holder.cardView.setOnClickListener(View.OnClickListener {

            var wasPending = false
            for (item   : ListItem  in  data)
                if (!item.strikedOut)
                    wasPending = true

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
            fragment?.myViewModel?.updItem(item)

            var listIsPending   =   0

            for (item   : ListItem  in  data)
                if (!item.strikedOut)
                    listIsPending++

//            if (!listIsPending) {
//                fragment?.myViewModel?.updListState(data[0].listId, listIsPending>0)
//            }

            if (wasPending!= (listIsPending>0))
                fragment?.myViewModel?.updListState(data[0].listId, listIsPending>0)
        })
    }


}