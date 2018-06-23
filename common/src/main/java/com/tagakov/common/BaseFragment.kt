package com.tagakov.common

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {
    protected val bind = createBinder()

    override fun onDestroyView() {
        bind.resetViews()
        super.onDestroyView()
    }
}