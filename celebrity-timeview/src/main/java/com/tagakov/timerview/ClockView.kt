package com.tagakov.timerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.tagakov.common.extractAttributes
import com.tagakov.timelogic.Time
import kotlin.math.min
import kotlin.properties.Delegates.observable



class TimeView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var clockColor by observable(Color.RED) { _, _, _ -> invalidate() }
    var time by observable(Time(3, 0)) { _, _, _ -> invalidate() }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        extractAttributes(attrs, R.styleable.TimeView, defStyleAttr, defStyleRes) {
            clockColor = getColor(R.styleable.TimeView_clockColor, clockColor)
            time = Time(getInteger(R.styleable.TimeView_hours, time.hours), getInteger(R.styleable.TimeView_minutes, time.minutes))
        }

        with(paint) {
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }
    }

    override fun onDraw(canvas: Canvas) {
        fun getAngle(value: Int) = Math.PI * value / 30 - Math.PI / 2

        val cX = measuredWidth / 2f
        val cY = measuredHeight / 2f
        val sideWidth = min(cX, cY)
        val lineThickness = sideWidth * 0.15f

        paint.color = clockColor
        paint.strokeWidth = lineThickness

        canvas.drawCircle(cX, cY, sideWidth - lineThickness, paint)

        val hourAngle = getAngle(time.hours % 12 * 5)
        val hourHandWidth = (sideWidth - lineThickness * 2) * .8f
        canvas.drawLine(cX, cY, cX + Math.cos(hourAngle).toFloat() * hourHandWidth, cY + Math.sin(hourAngle).toFloat() * hourHandWidth, paint)

        val minuteAngle = getAngle(time.minutes % 60)
        val minuteHandWidth = (sideWidth - lineThickness * 2) * .9f
        canvas.drawLine(cX, cY, cX + Math.cos(minuteAngle).toFloat() * minuteHandWidth, cY + Math.sin(minuteAngle).toFloat() * minuteHandWidth, paint)
    }
}