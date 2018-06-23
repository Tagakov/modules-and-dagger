package com.tagakov.userscreen.di

import com.tagakov.userscreen.UserScreenFragment
import com.tagakov.userscreen.api.UserScreenDependencies
import dagger.Component

@Component(dependencies = [UserScreenDependencies::class])
internal interface UserScreenComponent {
    fun inject(userScreenFragment: UserScreenFragment)
}