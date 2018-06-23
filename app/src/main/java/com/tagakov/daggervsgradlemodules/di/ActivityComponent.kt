package com.tagakov.daggervsgradlemodules.di

import android.support.v4.app.FragmentManager
import com.tagakov.common.ComponentDependencies
import com.tagakov.common.ComponentDependenciesKey
import com.tagakov.common.di.Rx
import com.tagakov.daggervsgradlemodules.MainActivity
import com.tagakov.daggervsgradlemodules.NavigationManager
import com.tagakov.startscreen.api.StartScreenDependencies
import com.tagakov.startscreen.api.StartScreenNavigator
import com.tagakov.startscreen.api.UserNameProvider
import com.tagakov.timelogic.TimelogicComponent
import com.tagakov.userapi.UserService
import com.tagakov.userscreen.api.UserScreenDependencies
import com.tagakov.userscreen.api.UserpicLinkProvider
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class MainActivityScope

@MainActivityScope
@Component(
        modules = [BindingsModule::class, Module::class, ComponentDependenciesModule::class],
        dependencies = [TimelogicComponent::class]
)
internal interface MainComponent : StartScreenDependencies, UserScreenDependencies {
    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        fun timelogicComponent(timelogicComponent: TimelogicComponent): Builder
        @BindsInstance
        fun bindFragmentManager(manager: FragmentManager): Builder
        fun build(): MainComponent
    }
}

@dagger.Module
private object Module {
    @Provides
    @JvmStatic
    @MainActivityScope
    fun provideUserService() = UserService()

    @Provides
    @JvmStatic
    fun provideUserNameProvider(userService: UserService) = object : UserNameProvider {
        override val userName: String
            get() = userService.getUser().run { "$firstName $lastName" }
    }

    @Provides
    @JvmStatic
    fun provideUserpicLinkProvider(userService: UserService) = object : UserpicLinkProvider {
        override val userpic: String
            get() = userService.getUser().userpic
    }

    @Provides
    @JvmStatic
    @Rx.MainScheduler
    fun provideMainScheduler() = AndroidSchedulers.mainThread()
}

@dagger.Module
private abstract class BindingsModule {
    @Binds
    abstract fun provideStartScreenNavigator(navigationManager: NavigationManager): StartScreenNavigator

}

@dagger.Module
private abstract class ComponentDependenciesModule private constructor() {
    @Binds @IntoMap @ComponentDependenciesKey(StartScreenDependencies::class)
    abstract fun provideStartScreenDependencies(component: MainComponent): ComponentDependencies

    @Binds @IntoMap @ComponentDependenciesKey(UserScreenDependencies::class)
    abstract fun provideUserScreenDependencies(component: MainComponent): ComponentDependencies
}
