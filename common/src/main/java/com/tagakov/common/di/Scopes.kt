package com.tagakov.common.di

import javax.inject.Qualifier

class Rx private constructor() {
    @Qualifier
    annotation class MainScheduler
}