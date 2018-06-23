package com.tagakov.userscreen.presentation

import com.tagakov.timelogic.Time

internal interface UserScreenView {
    fun showUserpic(url: String)
    fun updateTime(time: Time)
}