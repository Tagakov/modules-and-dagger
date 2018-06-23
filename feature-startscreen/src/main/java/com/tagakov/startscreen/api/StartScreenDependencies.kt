package com.tagakov.startscreen.api

import com.tagakov.common.ComponentDependencies
import com.tagakov.common.di.Rx
import io.reactivex.Scheduler

interface StartScreenDependencies: ComponentDependencies {
    fun userNameProvider(): UserNameProvider
    fun startScreenNavigator(): StartScreenNavigator
    @Rx.MainScheduler fun mainThread(): Scheduler
}