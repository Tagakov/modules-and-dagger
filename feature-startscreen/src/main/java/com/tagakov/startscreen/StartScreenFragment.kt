package com.tagakov.startscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import com.tagakov.common.BaseFragment
import com.tagakov.common.findComponentDependencies
import com.tagakov.startscreen.di.DaggerStartScreenComponent
import com.tagakov.startscreen.presentation.Presenter
import com.tagakov.startscreen.presentation.StartScreenView
import com.tagakov.timelogic.Time
import com.tagakov.timelogic.TimelogicComponent
import com.tagakov.timelogic.builder
import com.tagakov.timerview.TimeView
import javax.inject.Inject

class StartScreenFragment : BaseFragment(), StartScreenView {

    private val username by bind<TextView>(R.id.start_screen_text)
    private val actionButton by bind<View>(R.id.start_screen_button)
    private val clock by bind<TimeView>(R.id.start_screen_clock)

    @Inject
    internal lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerStartScreenComponent.builder()
                .startScreenDependencies(findComponentDependencies())
                .timelogicComponent(createTimelogicComponent())
                .build()
                .inject(this)
        super.onCreate(savedInstanceState)
    }

    private fun createTimelogicComponent() = TimelogicComponent.builder().timeSpeed(10f).build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.start_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = presenter.bind(this)

    override fun onDestroyView() {
        presenter.unbind()
        super.onDestroyView()
    }

    override fun actionClicks() = actionButton.clicks()

    override fun showUsername(name: String) {
        username.text = name
    }

    override fun updateTime(time: Time) {
        clock.time = time
    }

}