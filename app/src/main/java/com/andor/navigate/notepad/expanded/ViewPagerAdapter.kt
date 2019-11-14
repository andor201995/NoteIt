package com.andor.navigate.notepad.expanded

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andor.navigate.notepad.R
import com.andor.navigate.notepad.core.MyDiffCallback
import com.andor.navigate.notepad.core.Utils
import com.andor.navigate.notepad.listing.dao.NoteModel
import java.util.*

class ViewPagerAdapter(
    private val context: Context,
    private val fragCallback: (ViewPageItemEvent) -> Unit
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPageHolder>() {

    lateinit var pageList: ArrayList<NoteModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPageHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_viewpage, parent, false)
        return ViewPageHolder(itemView)
    }

    override fun getItemCount(): Int = pageList.size

    override fun onBindViewHolder(holder: ViewPageHolder, position: Int) {
        holder.expandedText.text = pageList[position].body
        holder.expandedText.movementMethod = ScrollingMovementMethod()
        holder.itemContainer.background = Utils.getBackGroundRes(context, pageList[position].bg)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateRecyclerView(newPageList: List<NoteModel>) {
        val diffResult = DiffUtil.calculateDiff(
            MyDiffCallback(
                this.pageList,
                newPageList
            )
        )
        this.pageList.clear()
        this.pageList.addAll(newPageList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewPageHolder(val view: View) : RecyclerView.ViewHolder(view),
        View.OnTouchListener {
        val expandedText: TextView = view.findViewById(R.id.expandedNoteTxt)
        val itemContainer: View = view.findViewById(R.id.viewPageItemContainer)

        init {
            expandedText.setOnTouchListener(this)
        }

        private val gestureDetector =
            GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    fragCallback.invoke(
                        ViewPageItemEvent.DoubleClickEvent(
                            adapterPosition
                        )
                    )
                    return true
                }
            })

        override fun onTouch(targetView: View?, motionEvent: MotionEvent?): Boolean {
            gestureDetector.onTouchEvent(motionEvent)
            return true
        }
    }
}

sealed class ViewPageItemEvent {
    data class DoubleClickEvent(val adapterPosition: Int) : ViewPageItemEvent()
}
