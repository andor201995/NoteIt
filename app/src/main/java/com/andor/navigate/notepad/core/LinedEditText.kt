package com.andor.navigate.notepad.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue

import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

import com.andor.navigate.notepad.R

class LinedEditText// we need this constructor for LayoutInflater
    (context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    private val mDisplayMetrics: DisplayMetrics = context.resources.displayMetrics
    private val mRect: Rect = Rect()
    private val mPaint: Paint = Paint()

    init {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = ContextCompat.getColor(context, R.color.white)
    }

    override fun onDraw(canvas: Canvas) {
        val textPadding =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, mDisplayMetrics)
        val padding = 3
        //for line's with text
        val count = lineCount
        val r = mRect
        val paint = mPaint
        for (i in 0 until count) {
            val baseline = getLineBounds(i, r)
            val startY = baseline + padding
            canvas.drawLine(
                r.left.toFloat(),
                startY.toFloat(),
                r.right.toFloat(),
                startY.toFloat(),
                paint
            )
        }
        //for line's without text
        val baseline0 = (getLineBounds(0, r) - textPadding).toInt()
        val baselineN = getLineBounds(count - 1, r)

        val restLineCount = (measuredHeight - baselineN) / baseline0

        for (i in 0 until restLineCount) {
            val startY = baselineN + (i + 1) * baseline0 + padding
            canvas.drawLine(
                r.left.toFloat(),
                startY.toFloat(),
                r.right.toFloat(),
                startY.toFloat(),
                paint
            )
        }
        super.onDraw(canvas)
    }
}