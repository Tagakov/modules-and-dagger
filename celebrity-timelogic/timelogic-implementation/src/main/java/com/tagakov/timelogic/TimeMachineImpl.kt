package com.tagakov.timelogic

import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class TimeMachineImpl @Inject constructor(private val speed: Float) : TimeMachine {
    override fun timeline(): Observable<Time> {
        return Observable.interval((1000/speed).toLong(), TimeUnit.MILLISECONDS)
                .scan(Time(0,0)) { time, _ ->
                    val newMinutes = time.minutes + 1
                    Time(time.hours + newMinutes / 60, newMinutes % 60)
                }
    }
}