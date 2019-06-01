package com.andor.navigate.notepad.listing.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.putAll
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.listing.dao.NoteModel


class ListingAdapter(
    private val context: Context,
    private val noteList: SparseArray<MutableMap.MutableEntry<String, NoteModel>>,
    val fragCallback: (ListItemEvent) -> Unit

) : RecyclerView.Adapter<ListingAdapter.ListingHolder>() {

    override fun getItemCount(): Int {
        return noteList.size()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.listing_item, parent, false)
        return ListingHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListingHolder, position: Int) {

        holder.headTxtView.text = noteList[position].value.noteHead
        holder.bodyTxtView.text = noteList[position].value.noteBody
        holder.selectedItemView.inVisible()

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateRecyclerView(newNoteList: SparseArray<MutableMap.MutableEntry<String, NoteModel>>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(this.noteList, newNoteList))
        noteList.clear()
        noteList.putAll(newNoteList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ListingHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener,
        View.OnLongClickListener {
        val headTxtView: TextView = view.findViewById(R.id.noteHeadTxtView)
        val bodyTxtView: TextView = view.findViewById(R.id.noteBodyTxtView)
        val selectedItemView: View = view.findViewById(R.id.selectedItemView)

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            selectedItemView.visible()
            val noteModel = noteList[adapterPosition].key
            fragCallback.invoke(ListItemEvent.LongClickEvent(noteModel))
            return true
        }

        override fun onClick(v: View?) {
            if (selectedItemView.isVisible()) {
                selectedItemView.inVisible()
            } else {
                selectedItemView.visible()
            }
            val noteModel = noteList[adapterPosition].key
            fragCallback.invoke(ListItemEvent.SingleClickEvent(noteModel))
        }
    }
}

class MyDiffCallback(
    private val oldNoteList: SparseArray<MutableMap.MutableEntry<String, NoteModel>>,
    private val newNoteList: SparseArray<MutableMap.MutableEntry<String, NoteModel>>
) :
    DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition].key == newNoteList[newItemPosition].key
    }

    override fun getOldListSize(): Int {
        return oldNoteList.size()
    }

    override fun getNewListSize(): Int {
        return newNoteList.size()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition] == newNoteList[newItemPosition];
    }

}

sealed class ListItemEvent {
    data class SingleClickEvent(val noteModel: String) : ListItemEvent()
    data class LongClickEvent(val noteModel: String) : ListItemEvent()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}