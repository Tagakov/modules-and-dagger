package com.tagakov.daggervsgradlemodules

import android.support.v4.app.FragmentManager
import com.tagakov.daggervsgradlemodules.di.MainActivityScope
import com.tagakov.startscreen.api.StartScreenNavigator
import com.tagakov.userscreen.UserScreenFragment
import javax.inject.Inject

@MainActivityScope
class NavigationManager @Inject constructor(private val fragmentManager: FragmentManager): StartScreenNavigator {
    override fun goToUserScreen() {
        fragmentManager.beginTransaction()
                .replace(R.id.content, UserScreenFragment())
                .addToBackStack(null)
                .commit()
    }

}