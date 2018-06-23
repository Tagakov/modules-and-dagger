package com.tagakov.userscreen.presentation

import com.tagakov.common.di.Rx
import com.tagakov.common.plusAssign
import com.tagakov.timelogic.TimeMachine
import com.tagakov.userscreen.api.UserpicLinkProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

internal class Presenter @Inject constructor(
        private val userpicLinkProvider: UserpicLinkProvider,
        @Rx.MainScheduler private val mainThread: Scheduler,
        private val timeMachine: TimeMachine
) {

    private val bag = CompositeDisposable()

    fun bind(view: UserScreenView) {
        view.showUserpic(userpicLinkProvider.userpic)
        bag += timeMachine.timeline()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.updateTime(it) }
    }

    fun unbind() = bag.clear()

}
