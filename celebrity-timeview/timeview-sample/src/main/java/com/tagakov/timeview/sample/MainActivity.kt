package com.tagakov.timeview.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tagakov.common.bindView
import com.tagakov.timelogic.TimelogicComponent
import com.tagakov.timelogic.builder
import com.tagakov.timerview.TimeView
import io.reactivex.android.schedulers.AndroidSchedulers

class MainActivity : AppCompatActivity() {

    private val clock by bindView<TimeView>(R.id.clock)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TimelogicComponent.builder()
                .timeSpeed(10f)
                .build()
                .timeMachine()
                .timeline()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { clock.time = it }
    }
}
