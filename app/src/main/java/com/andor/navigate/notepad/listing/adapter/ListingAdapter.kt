package com.andor.navigate.notepad.listing.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.SortingType
import com.andor.navigate.notepad.core.Utils
import com.andor.navigate.notepad.listing.dao.NoteModel
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class ListingAdapter(
    private val context: Context,
    private val noteList: List<NoteModel>,
    private var sortingType: SortingType,
    val fragCallback: (ListItemEvent) -> Unit

) : RecyclerView.Adapter<ListingAdapter.ListingHolder>(), Filterable {

    init {
        sortList(noteList)
    }

    private val noteFilterFullList = ArrayList<NoteModel>(noteList)

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterList = ArrayList<NoteModel>()
            if (constraint == null || constraint.isEmpty()) {
                filterList.addAll(noteFilterFullList)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()

                for (note in noteFilterFullList) {
                    if (note.head.toLowerCase().contains(filterPattern) ||
                        note.body.toLowerCase().contains(filterPattern)
                    ) {
                        filterList.add(note)
                    }
                }

            }
            sortList(filterList)
            val filterResults = FilterResults()
            filterResults.values = filterList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            if (filterResults != null) {
                (noteList as ArrayList).clear()
                noteList.addAll(filterResults.values as List<NoteModel>)
                notifyDataSetChanged()
            }
        }

    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false)
        return ListingHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListingHolder, position: Int) {
        val currentNoteModal = noteList[position]
        holder.headTxtView.text = currentNoteModal.head
        holder.bodyTxtView.text = currentNoteModal.body
        holder.dateCreatedTxtView.text =
            DateFormat.getDateInstance().format(Date(currentNoteModal.dateCreated))
        holder.container.background = Utils.getBackGroundRes(context, currentNoteModal.bg)
        holder.selectedItemView.inVisible()

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateRecyclerView(newNoteList: List<NoteModel>) {
        sortList(noteList)
        sortList(newNoteList)
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(this.noteList, newNoteList))
        (noteList as ArrayList).clear()
        noteList.addAll(newNoteList)
        noteFilterFullList.clear()
        noteFilterFullList.addAll(newNoteList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun sortList(noteList: List<NoteModel>) {
        val tempList = when (sortingType) {
            is SortingType.Alphabet -> noteList.sortedBy { it.head }
            is SortingType.DateUpdated -> noteList.sortedBy { -it.dateUpdated }
            is SortingType.DateCreated -> noteList.sortedBy { -it.dateCreated }
        }
        (noteList as ArrayList).clear()
        noteList.addAll(tempList)
    }

    override fun getFilter(): Filter {
        return filter
    }

    fun updateSortingType(sortingType: SortingType) {
        this.sortingType = sortingType
        notifyDataSetChanged()
    }

    inner class ListingHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener,
        View.OnLongClickListener {

        val headTxtView: TextView = view.findViewById(R.id.noteHeadTxtView)
        val bodyTxtView: TextView = view.findViewById(R.id.noteBodyTxtView)
        val dateCreatedTxtView: TextView = view.findViewById(R.id.noteDateTxtView)
        val selectedItemView: View = view.findViewById(R.id.selectedItemView)
        val container: View = view.findViewById(R.id.holder_container)

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            selectedItemView.visible()
            val noteModel = noteList[adapterPosition]
            fragCallback.invoke(ListItemEvent.LongClickEvent(noteModel))
            return true
        }

        override fun onClick(v: View?) {
            if (selectedItemView.isVisible()) {
                selectedItemView.inVisible()
            } else {
                selectedItemView.visible()
            }
            val noteModel = noteList[adapterPosition]
            fragCallback.invoke(ListItemEvent.SingleClickEvent(noteModel))
        }

    }


}

class MyDiffCallback(
    private val oldNoteList: List<NoteModel>,
    private val newNoteList: List<NoteModel>
) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition].id == newNoteList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldNoteList.size
    }

    override fun getNewListSize(): Int {
        return newNoteList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition] == newNoteList[newItemPosition]
    }

}

sealed class ListItemEvent {
    data class SingleClickEvent(val noteModel: NoteModel) : ListItemEvent()
    data class LongClickEvent(val noteModel: NoteModel) : ListItemEvent()
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
