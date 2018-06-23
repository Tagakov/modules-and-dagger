package com.tagakov.userscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.tagakov.common.BaseFragment
import com.tagakov.common.findComponentDependencies
import com.tagakov.timelogic.Time
import com.tagakov.timerview.TimeView
import com.tagakov.userscreen.di.DaggerUserScreenComponent
import com.tagakov.userscreen.presentation.Presenter
import com.tagakov.userscreen.presentation.UserScreenView
import javax.inject.Inject

class UserScreenFragment : BaseFragment(), UserScreenView {

    private val userpic by bind<ImageView>(R.id.user_screen_userpic)
    private val clock by bind<TimeView>(R.id.user_screen_clock)

    @Inject
    internal lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerUserScreenComponent.builder()
                .userScreenDependencies(findComponentDependencies())
                .build()
                .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = presenter.bind(this)

    override fun onDestroyView() {
        presenter.unbind()
        super.onDestroyView()
    }


    override fun showUserpic(url: String) = Picasso.get().load(url).into(userpic)

    override fun updateTime(time: Time) {
        clock.time = time
    }
}