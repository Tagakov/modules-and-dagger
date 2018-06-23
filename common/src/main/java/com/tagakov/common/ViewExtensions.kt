package com.tagakov.common

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View

inline fun View.extractAttributes(
        attrsSet: AttributeSet?,
        attrs: IntArray,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        block: TypedArray.() -> Unit
) = context.theme
        .obtainStyledAttributes(attrsSet, attrs, defStyleAttr, defStyleRes)
        .apply(block)
        .recycle()