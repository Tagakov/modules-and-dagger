package com.tagakov.daggervsgradlemodules

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tagakov.common.ComponentDependenciesProvider
import com.tagakov.common.HasComponentDependencies
import com.tagakov.daggervsgradlemodules.di.DaggerMainComponent
import com.tagakov.startscreen.StartScreenFragment
import com.tagakov.timelogic.TimelogicComponent
import com.tagakov.timelogic.builder
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasComponentDependencies {

    @Inject
    override lateinit var dependencies: ComponentDependenciesProvider
        protected set

    override fun onCreate(savedInstanceState: Bundle?) {
        performInject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, StartScreenFragment())
                    .commit()
        }
    }

    private fun performInject() {
        DaggerMainComponent.builder()
                .bindFragmentManager(supportFragmentManager)
                .timelogicComponent(TimelogicComponent.builder().timeSpeed(100f).build())
                .build()
                .inject(this)
    }
}
