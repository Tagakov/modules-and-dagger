package com.tagakov.timelogic

import io.reactivex.Observable

interface TimeMachine {
    fun timeline(): Observable<Time>
}