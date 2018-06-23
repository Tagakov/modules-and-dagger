package com.tagakov.timelogic

fun TimelogicComponent.Companion.builder(): TimelogicComponent.Builder {
    return DaggerTimelogicComponentImpl.builder()
}