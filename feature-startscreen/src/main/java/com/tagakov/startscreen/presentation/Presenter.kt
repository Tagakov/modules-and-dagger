package com.tagakov.startscreen.presentation

import com.tagakov.common.di.Rx
import com.tagakov.common.plusAssign
import com.tagakov.startscreen.api.StartScreenNavigator
import com.tagakov.startscreen.api.UserNameProvider
import com.tagakov.timelogic.TimeMachine
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

internal class Presenter @Inject constructor(
        private val userNameProvider: UserNameProvider,
        private val startScreenNavigator: StartScreenNavigator,
        @Rx.MainScheduler private val mainThread: Scheduler,
        private val timeMachine: TimeMachine
) {

    private val bag = CompositeDisposable()

    fun bind(view: StartScreenView) {
        view.showUsername(userNameProvider.userName)
        bag += view.actionClicks().subscribe { startScreenNavigator.goToUserScreen() }
        bag += timeMachine.timeline()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.updateTime(it) }
    }

    fun unbind() = bag.clear()

}
