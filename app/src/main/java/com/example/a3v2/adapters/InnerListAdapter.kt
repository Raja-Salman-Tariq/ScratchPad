package com.example.a3v2.adapters

import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.a3v2.R
import com.example.a3v2.BaseFragment
import com.example.a3v2.db.ListItem
import com.google.android.material.textfield.TextInputEditText
import android.app.Activity
import com.google.android.material.textfield.TextInputLayout


class InnerListAdapter(private val fragment: BaseFragment?, private val ctxt : Context, var data:MutableList<ListItem>)
    :    RecyclerView.Adapter<InnerListAdapter.MyViewHolder>(){

    val selectedItems   =   mutableSetOf<ListItem>()

    class MyViewHolder(view :   View,
                       val itemTxt  :   TextView    =   view.findViewById(R.id.rv_innerlist_item),
                       val editTxt  :   TextView   =   view.findViewById(R.id.rv_innerlist_item_edit_text),
                       val cardView :   CardView    =   view.findViewById(R.id.rv_innerlist_item_card),
                       val strikeOut:  ImageView    =   view.findViewById(R.id.rv_innerlist_item_strikethrough),
                       val timeStamp:   TextView    =   view.findViewById(R.id.rv_innerlist_ts),
                       val okBtn    :   ImageView   =   view.findViewById(R.id.rv_innerlist_ok),
                       val cancelBtn:   ImageView   =   view.findViewById(R.id.rv_innerlist_cancel),
                       val inpLayout:   TextInputLayout = view.findViewById(R.id.rv_innerlist_editlayout)
    )   :   RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.innerlist_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val listItem            =   data[position]
        holder.itemTxt.isCursorVisible=false

        // if list is temporary, deal with it in its special way and then return
        if (!listItem.concrete){
            holder.itemTxt.visibility=View.GONE
            holder.editTxt.visibility=View.VISIBLE
            holder.inpLayout.visibility=View.VISIBLE
            Log.d("chngeAddBtn", "onBindViewHolder: ")
            holder.editTxt.hint = "Enter new item"
            holder.editTxt.setHintTextColor(ContextCompat.getColor(ctxt,R.color.blue))
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
            holder.editTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.blue))
            holder.okBtn.visibility=View.VISIBLE
            holder.cancelBtn.visibility=View.VISIBLE
            holder.strikeOut.visibility = View.GONE
            holder.cardView.isClickable =   false
            holder.cardView.isFocusable =   false
            holder.cardView.isEnabled   =   false

//            holder.editTxt.inputType=InputType.TYPE_CLASS_TEXT
            holder.editTxt.isClickable=true
            holder.editTxt.isCursorVisible=true

            holder.okBtn.setOnClickListener{
                if (holder.editTxt.text.isNullOrEmpty())
                    holder.editTxt.error = "Enter item description."
                else {
                    data[position].mkConcrete(
                        fragment?.myViewModel!!,
                        holder.editTxt.text.toString()
                    )

                    (ctxt.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(fragment.view?.windowToken, 0)

                    holder.itemTxt.visibility=View.VISIBLE
                    holder.editTxt.visibility=View.GONE
                    holder.okBtn.visibility=View.GONE
                    holder.cancelBtn.visibility=View.GONE
                }
                holder.editTxt.text = null
            }

            holder.cancelBtn.setOnClickListener {
                if (!data[0].concrete) {
                    data.removeAt(0)
                    notifyItemRemoved(0)
                    (ctxt.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(fragment?.view?.windowToken, 0)
                    holder.editTxt.text = null

                }
            }

        }
        else {
            holder.itemTxt.visibility=View.VISIBLE
            holder.editTxt.visibility=View.GONE
            holder.inpLayout.visibility=View.GONE
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.blue))
            holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
            holder.okBtn.visibility=View.GONE
            holder.cancelBtn.visibility=View.GONE
            holder.strikeOut.visibility = View.GONE
            holder.cardView.isClickable =   true
            holder.cardView.isFocusable =   true
            holder.cardView.isEnabled   =   true


            holder.editTxt.isClickable=false
            holder.editTxt.isCursorVisible=false
           // safety ^
            holder.itemTxt.setText(listItem.text)
            holder.timeStamp.text = listItem.formattedTimestamp()

            //  frag null means this is second usage of adapter in the add button activity
            if (fragment == null) {
                holder.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        ctxt,
                        R.color.deeper_white_alt
                    )
                )
                holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.blue))
                holder.strikeOut.visibility = View.INVISIBLE

                // add or remove tapped selections in add activity
                holder.cardView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        val item: ListItem = data[holder.adapterPosition]
                        if (selectedItems.contains(item)) {
                            holder.cardView.setCardBackgroundColor(
                                ContextCompat.getColor(
                                    ctxt,
                                    R.color.deeper_white_alt
                                )
                            )
                            holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.blue))
                            selectedItems.remove(item)
                        } else {
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

            // set strikethrough
            if (listItem.strikedOut) {
                holder.itemTxt.tag = ctxt.resources.getString(R.string.striked_through)
                holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.red))
                holder.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        ctxt,
                        R.color.deeper_white_alt
                    )
                )
                holder.strikeOut.visibility = View.VISIBLE
                val params = holder.strikeOut.layoutParams
                params.width = holder.itemTxt.width + 25
                holder.strikeOut.layoutParams = params
                holder.strikeOut.requestLayout()
            } else {
                holder.itemTxt.tag = ctxt.resources.getString(R.string.not_striked_through)
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(ctxt, R.color.blue))
                holder.strikeOut.visibility = View.INVISIBLE
                holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.deeper_white_alt))
            }

            setListeners(holder, listItem)
        }
    }

    override fun getItemCount() =   data.size

    // get and set relevant data. Sort as well. Returns if list is pending, used by outer adapter to set how to display outer list (red or blue)
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

    // handle listening for deciding whether or not to strike through items
    private fun setListeners(holder: MyViewHolder, item: ListItem){


        holder.cardView.setOnClickListener {
            if (item.concrete) {

                var wasPending = false
                for (item: ListItem in data)
                    if (!item.strikedOut)
                        wasPending = true

                if (holder.itemTxt.tag.equals(ctxt.resources.getString(R.string.not_striked_through))) {    // clicked to strike through
                    holder.itemTxt.tag = ctxt.resources.getString(R.string.striked_through)
                    holder.itemTxt.setTextColor(ContextCompat.getColor(ctxt, R.color.red))
                    holder.cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            ctxt,
                            R.color.deeper_white_alt
                        )
                    )
                    holder.strikeOut.visibility = View.VISIBLE
                    val params = holder.strikeOut.layoutParams
                    params.width = holder.itemTxt.width + 25
                    holder.strikeOut.layoutParams = params
                    item.strikedOut = true
                } else {                                                                                      // clicked to unstrike through
                    holder.itemTxt.tag = ctxt.resources.getString(R.string.not_striked_through)
                    holder.cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            ctxt,
                            R.color.blue
                        )
                    )
                    holder.strikeOut.visibility = View.INVISIBLE
                    holder.itemTxt.setTextColor(
                        ContextCompat.getColor(
                            ctxt,
                            R.color.deeper_white_alt
                        )
                    )
                    item.strikedOut = false
                }
                fragment?.myViewModel?.updItem(item)

                var listIsPending = 0

                for (item: ListItem in data)
                    if (!item.strikedOut)
                        listIsPending++

                if (wasPending != (listIsPending > 0))
                    fragment?.myViewModel?.updListState(data[0].listId, listIsPending > 0)
            }
        }
    }

}