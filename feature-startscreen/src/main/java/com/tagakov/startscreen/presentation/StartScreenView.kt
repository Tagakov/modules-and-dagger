package com.tagakov.startscreen.presentation

import com.tagakov.timelogic.Time
import io.reactivex.Observable

internal interface StartScreenView {
    fun showUsername(name: String)
    fun updateTime(time: Time)

    fun actionClicks(): Observable<*>
}