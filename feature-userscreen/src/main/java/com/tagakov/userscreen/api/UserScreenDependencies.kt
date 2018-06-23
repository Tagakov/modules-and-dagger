package com.tagakov.userscreen.api

import com.tagakov.common.ComponentDependencies
import com.tagakov.common.di.Rx
import com.tagakov.timelogic.TimeMachine
import io.reactivex.Scheduler

interface UserScreenDependencies: ComponentDependencies {
    fun userpicLinkProvider(): UserpicLinkProvider
    fun timeMachine(): TimeMachine
    @Rx.MainScheduler fun mainThread(): Scheduler
}