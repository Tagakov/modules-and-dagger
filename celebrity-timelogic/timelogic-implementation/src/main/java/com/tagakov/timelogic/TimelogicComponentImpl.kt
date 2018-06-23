package com.tagakov.timelogic

import dagger.Binds
import dagger.BindsInstance
import dagger.Component

@Component(modules = [Module::class])
interface TimelogicComponentImpl: TimelogicComponent {

    @Component.Builder
    interface Builder: TimelogicComponent.Builder {
        @BindsInstance
        override fun timeSpeed(speed: Float): Builder
    }
}

@dagger.Module
internal interface Module {
    @Binds
    fun provideTimeMachine(timeMachineImpl: TimeMachineImpl): TimeMachine
}