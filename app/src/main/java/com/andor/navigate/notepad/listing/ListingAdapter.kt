package com.andor.navigate.notepad.listing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.andor.navigate.notepad.R


class ListingAdapter(
    private val context: Context,
    private val contentList: HashMap<Int, ListModel>

) : RecyclerView.Adapter<ListingAdapter.ListingHolder>() {

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.listing_item, parent, false)
        return ListingHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListingHolder, position: Int) {
        holder.headTxtView.text = contentList[position]!!.noteHead
        holder.bodyTxtView.text = contentList[position]!!.noteBody

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ListingHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val listModel = contentList[adapterPosition]!!
            val action =
                NoteListingFragmentDirections.actionNoteListingFragmentToExpandedNoteFragment(
                    listModel.noteHead,
                    listModel.noteBody
                )
            Navigation.findNavController(v!!).navigate(action)
        }

        val headTxtView: TextView = view.findViewById(R.id.noteHeadTxtView)
        val bodyTxtView: TextView = view.findViewById(R.id.noteBodyTxtView)
    }
}

data class ListModel(val noteHead: String = "Default Head", val noteBody: String = "Default Body")
