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

class BasicRvAdapter(private val ctxt : Context, private val data:MutableList<String>)
    :    RecyclerView.Adapter<BasicRvAdapter.MyViewHolder>(){

    class MyViewHolder(view :   View,
                       val itemTxt  :   TextView    =   view.findViewById(R.id.basic_rv_tv)
    )   :   RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.basic_rv_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemTxt.text  =   data[position]
    }

    override fun getItemCount() =   data.size


}