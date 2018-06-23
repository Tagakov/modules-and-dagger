package com.tagakov.timelogic

interface TimelogicComponent {

    companion object

    fun timeMachine(): TimeMachine

    interface Builder {
        fun timeSpeed(speed: Float): Builder
        fun build(): TimelogicComponent
    }
}